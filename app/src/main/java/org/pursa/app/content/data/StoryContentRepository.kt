package org.pursa.app.content.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.pursa.app.content.model.PursaStory
import org.pursa.app.content.model.PursaStorySummary
import org.pursa.app.content.model.toSummary
import org.pursa.app.content.validation.StoryContentValidator
import org.pursa.app.content.validation.StoryValidationError
import org.pursa.app.content.validation.StoryValidationErrorCode
import org.pursa.app.content.validation.StoryValidationResult

interface StoryContentRepository {
    suspend fun loadAllStorySummaries(): StoryContentResult<List<PursaStorySummary>>
    suspend fun loadStoriesByWorld(worldId: String): StoryContentResult<List<PursaStorySummary>>
    suspend fun loadStory(storyId: String): StoryContentResult<PursaStory>
}

class LocalStoryContentRepository(
    private val dataSource: StoryDataSource,
    private val parser: JsonStoryParser = JsonStoryParser(),
    private val validator: StoryContentValidator = StoryContentValidator(),
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val manifestPath: String = ManifestPath,
) : StoryContentRepository {
    private var manifestCache: ContentManifest? = null
    private val storyCache = mutableMapOf<String, StoryContentResult<PursaStory>>()

    override suspend fun loadAllStorySummaries(): StoryContentResult<List<PursaStorySummary>> = withContext(ioDispatcher) {
        val manifest = loadManifest()
        if (manifest !is StoryContentResult.Success) return@withContext manifest.castFailure()

        val stories = mutableListOf<PursaStorySummary>()
        manifest.value.stories.forEach { entry ->
            when (val result = loadStoryInternal(entry)) {
                is StoryContentResult.Success -> stories += result.value.toSummary()
                is StoryContentResult.InvalidContent -> return@withContext result
                is StoryContentResult.ReadFailure -> return@withContext result
                StoryContentResult.NotFound -> return@withContext StoryContentResult.NotFound
            }
        }
        StoryContentResult.Success(stories)
    }

    override suspend fun loadStoriesByWorld(worldId: String): StoryContentResult<List<PursaStorySummary>> = withContext(ioDispatcher) {
        val all = loadAllStorySummaries()
        when (all) {
            is StoryContentResult.Success -> StoryContentResult.Success(all.value.filter { it.worldId == worldId })
            is StoryContentResult.InvalidContent -> all
            is StoryContentResult.ReadFailure -> all
            StoryContentResult.NotFound -> StoryContentResult.Success(emptyList())
        }
    }

    override suspend fun loadStory(storyId: String): StoryContentResult<PursaStory> = withContext(ioDispatcher) {
        val manifest = loadManifest()
        if (manifest !is StoryContentResult.Success) return@withContext manifest.castFailure()

        val entry = manifest.value.stories.firstOrNull { it.id == storyId }
            ?: return@withContext StoryContentResult.NotFound
        loadStoryInternal(entry)
    }

    private fun loadManifest(): StoryContentResult<ContentManifest> {
        manifestCache?.let { return StoryContentResult.Success(it) }
        val text = dataSource.readText(manifestPath)
        if (text is StoryDataReadResult.Failure) return StoryContentResult.ReadFailure(text.message)

        val parsed = parser.parseManifest((text as StoryDataReadResult.Success).text)
        if (parsed is StoryParseResult.Failure) {
            return StoryContentResult.InvalidContent(
                listOf(parseError("manifest", StoryValidationErrorCode.ParseFailure, "${parsed.code.name}: ${parsed.message}")),
            )
        }

        val manifest = (parsed as StoryParseResult.Success).value
        if (manifest.schemaVersion != StoryContentValidator.SupportedSchemaVersion || manifest.locale != "fa") {
            return StoryContentResult.InvalidContent(
                listOf(parseError("manifest", StoryValidationErrorCode.InvalidManifest, "Manifest schema version or locale is unsupported.")),
            )
        }
        val manifestErrors = validateManifest(manifest)
        if (manifestErrors.isNotEmpty()) return StoryContentResult.InvalidContent(manifestErrors)

        manifestCache = manifest
        return StoryContentResult.Success(manifest)
    }

    private fun validateManifest(manifest: ContentManifest): List<StoryValidationError> = buildList {
        manifest.stories
            .groupBy { it.id }
            .filterValues { it.size > 1 }
            .keys
            .forEach { storyId ->
                add(parseError(storyId, StoryValidationErrorCode.InvalidManifest, "Manifest story ID must be unique."))
            }
        manifest.stories
            .groupBy { it.assetPath }
            .filterValues { it.size > 1 }
            .keys
            .forEach { assetPath ->
                add(parseError("manifest", StoryValidationErrorCode.InvalidManifest, "Manifest asset path must be unique: $assetPath"))
            }
    }

    private fun loadStoryInternal(entry: ContentManifestStory): StoryContentResult<PursaStory> {
        storyCache[entry.id]?.let { return it }
        val result = when (val text = dataSource.readText(entry.assetPath)) {
            is StoryDataReadResult.Failure -> StoryContentResult.ReadFailure(text.message)
            is StoryDataReadResult.Success -> parseAndValidateStory(entry, text.text)
        }
        storyCache[entry.id] = result
        return result
    }

    private fun parseAndValidateStory(
        entry: ContentManifestStory,
        rawJson: String,
    ): StoryContentResult<PursaStory> {
        val parsed = parser.parseStory(rawJson)
        if (parsed is StoryParseResult.Failure) {
            return StoryContentResult.InvalidContent(
                listOf(parseError(entry.id, StoryValidationErrorCode.ParseFailure, "${parsed.code.name}: ${parsed.message}")),
            )
        }

        val story = (parsed as StoryParseResult.Success).value
        val manifestErrors = buildList {
            if (story.id != entry.id) {
                add(parseError(entry.id, StoryValidationErrorCode.InvalidManifest, "Story ID does not match manifest entry."))
            }
            if (story.worldId != entry.worldId) {
                add(parseError(entry.id, StoryValidationErrorCode.InvalidManifest, "Story world ID does not match manifest entry."))
            }
        }
        if (manifestErrors.isNotEmpty()) return StoryContentResult.InvalidContent(manifestErrors)

        return when (val validation = validator.validate(story)) {
            StoryValidationResult.Valid -> StoryContentResult.Success(story)
            is StoryValidationResult.Invalid -> StoryContentResult.InvalidContent(validation.errors)
        }
    }

    private fun parseError(
        storyId: String?,
        code: StoryValidationErrorCode,
        message: String,
    ): StoryValidationError = StoryValidationError(
        storyId = storyId,
        stepId = null,
        code = code,
        message = message,
    )

    @Suppress("UNCHECKED_CAST")
    private fun <T> StoryContentResult<*>.castFailure(): StoryContentResult<T> = this as StoryContentResult<T>

    companion object {
        const val ManifestPath = "content/fa/manifest.json"
    }
}

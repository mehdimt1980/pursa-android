package org.pursa.app.content

import java.nio.file.Files
import java.nio.file.Path
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.pursa.app.content.data.ContentManifestStory
import org.pursa.app.content.data.JsonStoryParser
import org.pursa.app.content.data.StoryParseResult
import org.pursa.app.content.model.PursaStory
import org.pursa.app.content.model.PursaStoryOption
import org.pursa.app.content.model.PursaStoryStep
import org.pursa.app.content.validation.StoryContentValidator
import org.pursa.app.content.validation.StoryValidationResult

class ProductionStoryContentTest {
    private val parser = JsonStoryParser()
    private val validator = StoryContentValidator()
    private val manifest = productionManifest()

    @Test
    fun manifestContainsExactlyEightStoriesInPedagogicalOrder() {
        assertEquals(productionStoryIds, manifest.stories.map { it.id })
        assertEquals(productionTruthStoryIds, manifest.stories.filter { it.worldId == "truth" }.map { it.id })
        assertEquals(productionJusticeStoryIds, manifest.stories.filter { it.worldId == "justice" }.map { it.id })
        assertEquals(manifest.stories.size, manifest.stories.map { it.id }.toSet().size)
        assertEquals(manifest.stories.size, manifest.stories.map { it.assetPath }.toSet().size)
    }

    @Test
    fun everyManifestAssetExists() {
        manifest.stories.forEach { entry ->
            assertTrue("Missing asset: ${entry.assetPath}", Files.exists(projectAssetPath(entry.assetPath)))
        }
    }

    @Test
    fun everyRegisteredProductionStoryParsesAndValidates() {
        parsedStories().forEach { (entry, story) ->
            assertEquals(entry.id, story.id)
            assertEquals(entry.worldId, story.worldId)
            assertEquals(StoryValidationResult.Valid, validator.validate(story))
        }
    }

    @Test
    fun filteringWorldInventoryContainsExpectedMissions() {
        val grouped = manifest.stories.groupBy { it.worldId }

        assertEquals(4, grouped["truth"]?.size)
        assertEquals(4, grouped["justice"]?.size)
        assertTrue(grouped["friendship"].orEmpty().isEmpty())
    }

    @Test
    fun everyProductionMissionContainsRequiredPhilosophicalStructure() {
        parsedStories().forEach { (_, story) ->
            assertTrue(story.steps.any { it is PursaStoryStep.Narrative })
            assertTrue(story.steps.any { it is PursaStoryStep.ReasonPrompt })
            assertTrue(story.steps.any { it is PursaStoryStep.Perspective })
            assertTrue(story.steps.any { it is PursaStoryStep.Counterexample })
            assertTrue(story.steps.any { it is PursaStoryStep.Reflection })
            assertTrue(story.steps.any { it.selectableOptions().isNotEmpty() })
            assertTrue(story.completion.reflection.isNotBlank())
        }
    }

    @Test
    fun everyProductionMissionSatisfiesOptionQualityInvariants() {
        parsedStories().forEach { (_, story) ->
            assertTrue(story.steps.isNotEmpty())
            assertTrue(story.contentRevision > 0)
            assertTrue(story.estimatedDurationMinutes > 0)
            assertTrue(story.recommendedMinAge in 6..18)
            assertTrue(story.recommendedMaxAge in story.recommendedMinAge..18)
            assertTrue(story.worldId in setOf("truth", "justice"))
            assertEquals(story.steps.size, story.steps.map { it.id }.toSet().size)

            story.steps.forEach { step ->
                val options = step.selectableOptions()
                assertEquals(options.size, options.map { it.id }.toSet().size)
                assertTrue(options.all { it.label.isNotBlank() })
            }
        }
    }

    @Test
    fun productionStoryJsonDoesNotContainScoringOrProfilingFields() {
        val forbiddenFieldNames = listOf("correct", "correctAnswer", "score", "points", "reward", "rank", "profile")

        manifest.stories.forEach { entry ->
            val rawJson = productionStoryJson(entry.assetPath)
            forbiddenFieldNames.forEach { field ->
                assertTrue("${entry.id} must not contain field $field", "\"$field\"" !in rawJson)
            }
        }
    }

    private fun parsedStories(): List<Pair<ContentManifestStory, PursaStory>> =
        manifest.stories.map { entry ->
            val result = parser.parseStory(productionStoryJson(entry.assetPath))
            assertTrue(result is StoryParseResult.Success)
            entry to (result as StoryParseResult.Success).value
        }

    private fun PursaStoryStep.selectableOptions(): List<PursaStoryOption> = when (this) {
        is PursaStoryStep.Narrative -> emptyList()
        is PursaStoryStep.SingleChoice -> options
        is PursaStoryStep.ReasonPrompt -> reasons
        is PursaStoryStep.Perspective -> responses
        is PursaStoryStep.Counterexample -> choices
        is PursaStoryStep.Reflection -> choices
    }

    private fun projectAssetPath(assetPath: String): Path {
        val userDir = Path.of(System.getProperty("user.dir"))
        val candidates = listOf(
            userDir.resolve("app/src/main/assets/$assetPath"),
            userDir.resolve("src/main/assets/$assetPath"),
            userDir.parent?.resolve("app/src/main/assets/$assetPath"),
        ).filterNotNull()
        return candidates.firstOrNull { Files.exists(it) } ?: candidates.first()
    }
}

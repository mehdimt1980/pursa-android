package org.pursa.app.content

import java.nio.file.Files
import java.nio.file.Path
import org.pursa.app.content.data.ContentManifest
import org.pursa.app.content.data.JsonStoryParser
import org.pursa.app.content.data.StoryParseResult
import org.pursa.app.content.data.StoryDataReadResult
import org.pursa.app.content.data.StoryDataSource

val productionTruthStoryIds = listOf(
    "truth_broken_vase",
    "truth_group_photo",
    "truth_strange_news",
    "truth_friend_secret",
)

val productionJusticeStoryIds = listOf(
    "justice_last_cake",
    "justice_class_representative",
    "justice_playground_rule",
    "justice_team_prize",
)

val productionStoryIds = productionTruthStoryIds + productionJusticeStoryIds

fun sampleStoryJson(): String = readProjectFile(
    "app/src/main/assets/content/fa/stories/truth/truth_broken_vase.json",
    "src/main/assets/content/fa/stories/truth/truth_broken_vase.json",
)

fun sampleManifestJson(): String = readProjectFile(
    "app/src/main/assets/content/fa/manifest.json",
    "src/main/assets/content/fa/manifest.json",
)

fun productionManifest(): ContentManifest =
    (JsonStoryParser().parseManifest(sampleManifestJson()) as StoryParseResult.Success).value

fun productionStoryJson(assetPath: String): String = readProjectFile(
    "app/src/main/assets/$assetPath",
    "src/main/assets/$assetPath",
)

fun productionStoryAssets(): Map<String, String> =
    productionManifest().stories.associate { entry ->
        entry.assetPath to productionStoryJson(entry.assetPath)
    }

fun readProjectFile(
    rootRelativePath: String,
    moduleRelativePath: String,
): String {
    val userDir = Path.of(System.getProperty("user.dir"))
    val candidates = listOf(
        userDir.resolve(rootRelativePath),
        userDir.resolve(moduleRelativePath),
        userDir.parent?.resolve(rootRelativePath),
    ).filterNotNull()
    val path = candidates.first { Files.exists(it) }
    return String(Files.readAllBytes(path), Charsets.UTF_8)
}

class FakeStoryDataSource(
    private val assets: Map<String, String>,
) : StoryDataSource {
    override fun readText(assetPath: String): StoryDataReadResult = assets[assetPath]?.let {
        StoryDataReadResult.Success(it)
    } ?: StoryDataReadResult.Failure("missing")
}

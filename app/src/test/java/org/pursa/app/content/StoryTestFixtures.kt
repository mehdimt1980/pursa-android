package org.pursa.app.content

import java.nio.file.Files
import java.nio.file.Path
import org.pursa.app.content.data.StoryDataReadResult
import org.pursa.app.content.data.StoryDataSource

fun sampleStoryJson(): String = readProjectFile(
    "app/src/main/assets/content/fa/stories/truth/truth_broken_vase.json",
    "src/main/assets/content/fa/stories/truth/truth_broken_vase.json",
)

fun sampleManifestJson(): String = readProjectFile(
    "app/src/main/assets/content/fa/manifest.json",
    "src/main/assets/content/fa/manifest.json",
)

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
    return Files.readString(path)
}

class FakeStoryDataSource(
    private val assets: Map<String, String>,
) : StoryDataSource {
    override fun readText(assetPath: String): StoryDataReadResult = assets[assetPath]?.let {
        StoryDataReadResult.Success(it)
    } ?: StoryDataReadResult.Failure("missing")
}

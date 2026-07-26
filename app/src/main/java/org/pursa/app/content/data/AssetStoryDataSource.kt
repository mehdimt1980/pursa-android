package org.pursa.app.content.data

import android.content.res.AssetManager

class AssetStoryDataSource(
    private val assetManager: AssetManager,
) : StoryDataSource {
    override fun readText(assetPath: String): StoryDataReadResult = try {
        assetManager.open(assetPath).bufferedReader(Charsets.UTF_8).use { reader ->
            StoryDataReadResult.Success(reader.readText())
        }
    } catch (exception: Exception) {
        StoryDataReadResult.Failure(exception::class.simpleName ?: "ReadFailure")
    }
}

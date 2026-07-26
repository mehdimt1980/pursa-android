package org.pursa.app.content.data

sealed interface StoryDataReadResult {
    data class Success(val text: String) : StoryDataReadResult
    data class Failure(val message: String) : StoryDataReadResult
}

interface StoryDataSource {
    fun readText(assetPath: String): StoryDataReadResult
}

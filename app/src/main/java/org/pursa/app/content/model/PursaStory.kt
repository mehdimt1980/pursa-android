package org.pursa.app.content.model

import kotlinx.serialization.Serializable

@Serializable
data class PursaStory(
    val schemaVersion: Int,
    val id: String,
    val worldId: String,
    val title: String,
    val summary: String,
    val recommendedMinAge: Int,
    val recommendedMaxAge: Int,
    val estimatedDurationMinutes: Int,
    val themes: List<String>,
    val introduction: StoryIntroduction,
    val steps: List<PursaStoryStep>,
    val completion: StoryCompletion,
)

@Serializable
data class StoryIntroduction(
    val title: String,
    val body: String,
)

@Serializable
data class StoryCompletion(
    val title: String,
    val reflection: String,
    val familyPrompt: String? = null,
)

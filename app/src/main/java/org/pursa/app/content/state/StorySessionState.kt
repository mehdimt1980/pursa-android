package org.pursa.app.content.state

data class StorySessionState(
    val storyId: String,
    val currentStepIndex: Int,
    val selectedAnswers: Map<String, String>,
    val completed: Boolean,
)

package org.pursa.app.progress.model

data class SavedStorySession(
    val storyId: String,
    val contentRevision: Int,
    val currentStepIndex: Int,
    val selectedAnswers: Map<String, String>,
)

package org.pursa.app.journal.model

data class ReflectionJournalRecord(
    val storyId: String,
    val contentRevision: Int,
    val reflectionStepId: String,
    val selectedReflectionOptionId: String?,
    val revisitQuestionStepId: String,
    val completedAtEpochMillis: Long,
    val updatedAtEpochMillis: Long,
    val journalSchemaVersion: Int = CurrentSchemaVersion,
) {
    companion object {
        const val CurrentSchemaVersion = 1
    }
}

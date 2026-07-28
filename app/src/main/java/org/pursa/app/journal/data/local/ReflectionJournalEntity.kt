package org.pursa.app.journal.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reflection_journal")
data class ReflectionJournalEntity(
    @PrimaryKey val storyId: String,
    val contentRevision: Int,
    val reflectionStepId: String,
    val selectedReflectionOptionId: String?,
    val revisitQuestionStepId: String,
    val completedAtEpochMillis: Long,
    val updatedAtEpochMillis: Long,
    val journalSchemaVersion: Int,
)

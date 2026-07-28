package org.pursa.app.progress.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "story_sessions",
    foreignKeys = [
        ForeignKey(
            entity = MissionProgressEntity::class,
            parentColumns = ["storyId"],
            childColumns = ["storyId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class StorySessionEntity(
    @PrimaryKey val storyId: String,
    val currentStepIndex: Int,
    val contentRevision: Int,
    val sessionSchemaVersion: Int,
    val updatedAtEpochMillis: Long,
)

package org.pursa.app.progress.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mission_progress")
data class MissionProgressEntity(
    @PrimaryKey val storyId: String,
    val status: String,
    val startedAtEpochMillis: Long?,
    val updatedAtEpochMillis: Long?,
    val completedAtEpochMillis: Long?,
)

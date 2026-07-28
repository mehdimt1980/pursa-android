package org.pursa.app.progress.model

data class MissionProgress(
    val storyId: String,
    val status: MissionProgressStatus,
    val startedAtEpochMillis: Long?,
    val updatedAtEpochMillis: Long?,
    val completedAtEpochMillis: Long?,
)

fun notStartedProgress(storyId: String): MissionProgress = MissionProgress(
    storyId = storyId,
    status = MissionProgressStatus.NotStarted,
    startedAtEpochMillis = null,
    updatedAtEpochMillis = null,
    completedAtEpochMillis = null,
)

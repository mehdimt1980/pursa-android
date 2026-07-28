package org.pursa.app.progress.data.local

import org.pursa.app.progress.model.MissionProgress
import org.pursa.app.progress.model.MissionProgressStatus
import org.pursa.app.progress.model.SavedStorySession

fun MissionProgressEntity.toModel(): MissionProgress = MissionProgress(
    storyId = storyId,
    status = MissionProgressStatus.valueOf(status),
    startedAtEpochMillis = startedAtEpochMillis,
    updatedAtEpochMillis = updatedAtEpochMillis,
    completedAtEpochMillis = completedAtEpochMillis,
)

fun StorySessionWithAnswers.toModel(): SavedStorySession = SavedStorySession(
    storyId = session.storyId,
    contentRevision = session.contentRevision,
    currentStepIndex = session.currentStepIndex,
    selectedAnswers = answers.associate { it.stepId to it.selectedOptionId },
)

package org.pursa.app

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.pursa.app.content.model.PursaStory
import org.pursa.app.content.state.StorySessionState
import org.pursa.app.progress.data.MissionProgressRepository
import org.pursa.app.progress.data.MissionProgressResult
import org.pursa.app.progress.model.MissionProgress
import org.pursa.app.progress.model.SavedStorySession
import org.pursa.app.progress.model.notStartedProgress

class FakeMissionProgressRepository : MissionProgressRepository {
    override fun observeProgress(storyIds: List<String>): Flow<Map<String, MissionProgress>> =
        MutableStateFlow(storyIds.associateWith { notStartedProgress(it) })

    override fun observeProgress(storyId: String): Flow<MissionProgress> =
        MutableStateFlow(notStartedProgress(storyId))

    override suspend fun loadSavedSession(story: PursaStory): MissionProgressResult<SavedStorySession?> =
        MissionProgressResult.Success(null)

    override suspend fun saveSessionSnapshot(
        story: PursaStory,
        state: StorySessionState,
    ): MissionProgressResult<Unit> = MissionProgressResult.Success(Unit)

    override suspend fun markCompleted(story: PursaStory): MissionProgressResult<Unit> =
        MissionProgressResult.Success(Unit)

    override suspend fun clearActiveSession(storyId: String): MissionProgressResult<Unit> =
        MissionProgressResult.Success(Unit)

    override suspend fun clearAllLocalProgress(): MissionProgressResult<Unit> =
        MissionProgressResult.Success(Unit)
}

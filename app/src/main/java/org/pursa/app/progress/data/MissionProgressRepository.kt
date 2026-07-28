package org.pursa.app.progress.data

import kotlinx.coroutines.flow.Flow
import org.pursa.app.content.model.PursaStory
import org.pursa.app.content.state.StorySessionState
import org.pursa.app.progress.model.MissionProgress
import org.pursa.app.progress.model.SavedStorySession

interface MissionProgressRepository {
    fun observeProgress(storyIds: List<String>): Flow<Map<String, MissionProgress>>
    fun observeProgress(storyId: String): Flow<MissionProgress>
    suspend fun loadSavedSession(story: PursaStory): MissionProgressResult<SavedStorySession?>
    suspend fun saveSessionSnapshot(
        story: PursaStory,
        state: StorySessionState,
    ): MissionProgressResult<Unit>

    suspend fun markCompleted(story: PursaStory): MissionProgressResult<Unit>
    suspend fun clearActiveSession(storyId: String): MissionProgressResult<Unit>
    suspend fun clearAllLocalProgress(): MissionProgressResult<Unit>
}

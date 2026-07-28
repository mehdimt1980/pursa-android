package org.pursa.app.progress.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.pursa.app.content.model.PursaStory
import org.pursa.app.content.model.PursaStoryStep
import org.pursa.app.content.state.StorySessionState
import org.pursa.app.core.time.PursaClock
import org.pursa.app.core.time.SystemPursaClock
import org.pursa.app.progress.data.local.MissionProgressDao
import org.pursa.app.progress.data.local.toModel
import org.pursa.app.progress.model.MissionProgress
import org.pursa.app.progress.model.SavedStorySession
import org.pursa.app.progress.model.notStartedProgress

class LocalMissionProgressRepository(
    private val dao: MissionProgressDao,
    private val clock: PursaClock = SystemPursaClock,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : MissionProgressRepository {
    override fun observeProgress(storyIds: List<String>): Flow<Map<String, MissionProgress>> =
        dao.observeProgress(storyIds)
            .map { entities ->
                val persisted = entities.associate { it.storyId to it.toModel() }
                storyIds.associateWith { storyId -> persisted[storyId] ?: notStartedProgress(storyId) }
            }
            .catch {
                emit(storyIds.associateWith { storyId -> notStartedProgress(storyId) })
            }

    override fun observeProgress(storyId: String): Flow<MissionProgress> =
        dao.observeProgress(storyId)
            .map { entity -> entity?.toModel() ?: notStartedProgress(storyId) }
            .catch {
                emit(notStartedProgress(storyId))
            }

    override suspend fun loadSavedSession(story: PursaStory): MissionProgressResult<SavedStorySession?> = dbCall {
        val saved = dao.loadSession(story.id)?.toModel() ?: return@dbCall null
        if (saved.contentRevision != story.contentRevision || saved.currentStepIndex !in story.steps.indices) {
            dao.clearActiveSession(story.id, clock.nowEpochMillis())
            return@dbCall null
        }

        val validAnswers = saved.selectedAnswers.filter { (stepId, optionId) ->
            val step = story.steps.firstOrNull { it.id == stepId }
            step != null && optionId in step.optionIds()
        }
        if (validAnswers.size != saved.selectedAnswers.size) {
            dao.saveSession(
                storyId = story.id,
                currentStepIndex = saved.currentStepIndex,
                contentRevision = story.contentRevision,
                selectedAnswers = validAnswers,
                nowEpochMillis = clock.nowEpochMillis(),
            )
        }
        saved.copy(selectedAnswers = validAnswers)
    }

    override suspend fun saveSessionSnapshot(
        story: PursaStory,
        state: StorySessionState,
    ): MissionProgressResult<Unit> = dbCall {
        dao.saveSession(
            storyId = story.id,
            currentStepIndex = state.currentStepIndex.coerceIn(0, story.steps.lastIndex),
            contentRevision = story.contentRevision,
            selectedAnswers = state.selectedAnswers.filterValidFor(story),
            nowEpochMillis = clock.nowEpochMillis(),
        )
    }

    override suspend fun markCompleted(story: PursaStory): MissionProgressResult<Unit> = dbCall {
        dao.markCompleted(
            storyId = story.id,
            nowEpochMillis = clock.nowEpochMillis(),
        )
    }

    override suspend fun clearActiveSession(storyId: String): MissionProgressResult<Unit> = dbCall {
        dao.clearActiveSession(storyId, clock.nowEpochMillis())
    }

    override suspend fun clearAllLocalProgress(): MissionProgressResult<Unit> = dbCall {
        dao.clearAll()
    }

    private suspend fun <T> dbCall(block: suspend () -> T): MissionProgressResult<T> = try {
        MissionProgressResult.Success(withContext(ioDispatcher) { block() })
    } catch (_: Exception) {
        MissionProgressResult.Failure
    }

    private fun Map<String, String>.filterValidFor(story: PursaStory): Map<String, String> =
        filter { (stepId, optionId) ->
            val step = story.steps.firstOrNull { it.id == stepId }
            step != null && optionId in step.optionIds()
        }

    private fun PursaStoryStep.optionIds(): Set<String> = when (this) {
        is PursaStoryStep.Narrative -> emptySet()
        is PursaStoryStep.SingleChoice -> options.map { it.id }.toSet()
        is PursaStoryStep.ReasonPrompt -> reasons.map { it.id }.toSet()
        is PursaStoryStep.Perspective -> responses.map { it.id }.toSet()
        is PursaStoryStep.Counterexample -> choices.map { it.id }.toSet()
        is PursaStoryStep.Reflection -> choices.map { it.id }.toSet()
    }
}

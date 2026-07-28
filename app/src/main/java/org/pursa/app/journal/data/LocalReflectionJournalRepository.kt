package org.pursa.app.journal.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.pursa.app.content.model.PursaStory
import org.pursa.app.content.model.PursaStoryStep
import org.pursa.app.journal.data.local.ReflectionJournalDao
import org.pursa.app.journal.data.local.ReflectionJournalEntity
import org.pursa.app.journal.model.ReflectionJournalRecord
import org.pursa.app.journal.model.ResolvedJournalEntry

class LocalReflectionJournalRepository(
    private val dao: ReflectionJournalDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ReflectionJournalRepository {
    override fun observeEntries(): Flow<List<ReflectionJournalRecord>> =
        dao.observeEntries()
            .map { entries -> entries.map { it.toModel() } }
            .catch { emit(emptyList()) }

    override fun observeEntry(storyId: String): Flow<ReflectionJournalRecord?> =
        dao.observeEntry(storyId)
            .map { it?.toModel() }
            .catch { emit(null) }

    override suspend fun loadEntry(storyId: String): ReflectionJournalRecord? =
        withContext(ioDispatcher) { dao.loadEntry(storyId)?.toModel() }

    override suspend fun saveOrUpdateEntry(record: ReflectionJournalRecord): JournalResult<Unit> = dbCall {
        dao.upsertEntry(record.toEntity())
    }

    override suspend fun deleteEntry(storyId: String): JournalResult<Unit> = dbCall {
        dao.deleteEntry(storyId)
    }

    override suspend fun clearAllEntries(): JournalResult<Unit> = dbCall {
        dao.deleteAll()
    }

    override fun resolveEntry(
        record: ReflectionJournalRecord,
        story: PursaStory?,
    ): ResolvedJournalEntry {
        if (story == null) return ResolvedJournalEntry.StoryUnavailable(record)
        val question = story.journalQuestionCandidates().firstOrNull { it.stepId == record.revisitQuestionStepId }
        val reflectionStep = story.finalReflectionStep()
        val reflectionLabel = reflectionStep
            ?.choices
            ?.firstOrNull { it.id == record.selectedReflectionOptionId }
            ?.label

        return if (question == null || reflectionStep?.id != record.reflectionStepId) {
            ResolvedJournalEntry.Incompatible(
                record = record,
                storyTitle = story.title,
                worldId = story.worldId,
            )
        } else {
            ResolvedJournalEntry.Available(
                record = record,
                storyTitle = story.title,
                worldId = story.worldId,
                revisitQuestion = question.question,
                selectedReflectionLabel = reflectionLabel,
                contentChanged = record.contentRevision != story.contentRevision,
            )
        }
    }

    private suspend fun <T> dbCall(block: suspend () -> T): JournalResult<T> = try {
        JournalResult.Success(withContext(ioDispatcher) { block() })
    } catch (_: Exception) {
        JournalResult.Failure
    }

    private fun ReflectionJournalEntity.toModel(): ReflectionJournalRecord = ReflectionJournalRecord(
        storyId = storyId,
        contentRevision = contentRevision,
        reflectionStepId = reflectionStepId,
        selectedReflectionOptionId = selectedReflectionOptionId,
        revisitQuestionStepId = revisitQuestionStepId,
        completedAtEpochMillis = completedAtEpochMillis,
        updatedAtEpochMillis = updatedAtEpochMillis,
        journalSchemaVersion = journalSchemaVersion,
    )

    private fun ReflectionJournalRecord.toEntity(): ReflectionJournalEntity = ReflectionJournalEntity(
        storyId = storyId,
        contentRevision = contentRevision,
        reflectionStepId = reflectionStepId,
        selectedReflectionOptionId = selectedReflectionOptionId,
        revisitQuestionStepId = revisitQuestionStepId,
        completedAtEpochMillis = completedAtEpochMillis,
        updatedAtEpochMillis = updatedAtEpochMillis,
        journalSchemaVersion = journalSchemaVersion,
    )
}

fun PursaStory.journalQuestionCandidates(maxCount: Int = 5): List<org.pursa.app.journal.model.JournalQuestionCandidate> =
    steps.mapNotNull { step ->
        val question = when (step) {
            is PursaStoryStep.Narrative -> null
            is PursaStoryStep.SingleChoice -> step.question
            is PursaStoryStep.ReasonPrompt -> step.question
            is PursaStoryStep.Perspective -> step.question
            is PursaStoryStep.Counterexample -> step.question
            is PursaStoryStep.Reflection -> step.question
        }?.takeIf { it.isNotBlank() }
        question?.let {
            org.pursa.app.journal.model.JournalQuestionCandidate(
                stepId = step.id,
                question = it,
            )
        }
    }
        .distinctBy { it.stepId }
        .take(maxCount)

fun PursaStory.finalReflectionStep(): PursaStoryStep.Reflection? =
    steps.filterIsInstance<PursaStoryStep.Reflection>().lastOrNull()

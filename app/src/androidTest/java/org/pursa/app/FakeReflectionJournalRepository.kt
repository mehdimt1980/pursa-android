package org.pursa.app

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.pursa.app.content.model.PursaStory
import org.pursa.app.journal.data.JournalResult
import org.pursa.app.journal.data.ReflectionJournalRepository
import org.pursa.app.journal.model.ReflectionJournalRecord
import org.pursa.app.journal.model.ResolvedJournalEntry

class FakeReflectionJournalRepository : ReflectionJournalRepository {
    private val entries = MutableStateFlow<List<ReflectionJournalRecord>>(emptyList())

    override fun observeEntries(): Flow<List<ReflectionJournalRecord>> = entries

    override fun observeEntry(storyId: String): Flow<ReflectionJournalRecord?> =
        MutableStateFlow(entries.value.firstOrNull { it.storyId == storyId })

    override suspend fun loadEntry(storyId: String): ReflectionJournalRecord? =
        entries.value.firstOrNull { it.storyId == storyId }

    override suspend fun saveOrUpdateEntry(record: ReflectionJournalRecord): JournalResult<Unit> {
        entries.value = entries.value.filterNot { it.storyId == record.storyId } + record
        return JournalResult.Success(Unit)
    }

    override suspend fun deleteEntry(storyId: String): JournalResult<Unit> {
        entries.value = entries.value.filterNot { it.storyId == storyId }
        return JournalResult.Success(Unit)
    }

    override suspend fun clearAllEntries(): JournalResult<Unit> {
        entries.value = emptyList()
        return JournalResult.Success(Unit)
    }

    override fun resolveEntry(
        record: ReflectionJournalRecord,
        story: PursaStory?,
    ): ResolvedJournalEntry =
        if (story == null) {
            ResolvedJournalEntry.StoryUnavailable(record)
        } else {
            ResolvedJournalEntry.Incompatible(
                record = record,
                storyTitle = story.title,
                worldId = story.worldId,
            )
        }
}

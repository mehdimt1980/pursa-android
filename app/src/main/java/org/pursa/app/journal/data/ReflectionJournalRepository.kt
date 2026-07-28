package org.pursa.app.journal.data

import kotlinx.coroutines.flow.Flow
import org.pursa.app.content.model.PursaStory
import org.pursa.app.journal.model.ReflectionJournalRecord
import org.pursa.app.journal.model.ResolvedJournalEntry

interface ReflectionJournalRepository {
    fun observeEntries(): Flow<List<ReflectionJournalRecord>>
    fun observeEntry(storyId: String): Flow<ReflectionJournalRecord?>
    suspend fun loadEntry(storyId: String): ReflectionJournalRecord?
    suspend fun saveOrUpdateEntry(record: ReflectionJournalRecord): JournalResult<Unit>
    suspend fun deleteEntry(storyId: String): JournalResult<Unit>
    suspend fun clearAllEntries(): JournalResult<Unit>
    fun resolveEntry(record: ReflectionJournalRecord, story: PursaStory?): ResolvedJournalEntry
}

sealed interface JournalResult<out T> {
    data class Success<T>(val value: T) : JournalResult<T>
    data object Failure : JournalResult<Nothing>
}

package org.pursa.app.journal.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReflectionJournalDao {
    @Query("SELECT * FROM reflection_journal ORDER BY updatedAtEpochMillis DESC, storyId ASC")
    fun observeEntries(): Flow<List<ReflectionJournalEntity>>

    @Query("SELECT * FROM reflection_journal WHERE storyId = :storyId")
    fun observeEntry(storyId: String): Flow<ReflectionJournalEntity?>

    @Query("SELECT * FROM reflection_journal WHERE storyId = :storyId")
    suspend fun loadEntry(storyId: String): ReflectionJournalEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertEntry(entity: ReflectionJournalEntity)

    @Query("DELETE FROM reflection_journal WHERE storyId = :storyId")
    suspend fun deleteEntry(storyId: String)

    @Query("DELETE FROM reflection_journal")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM reflection_journal")
    suspend fun countEntries(): Int
}

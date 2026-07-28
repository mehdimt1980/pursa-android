package org.pursa.app.progress.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import org.pursa.app.progress.model.MissionProgressStatus

@Dao
abstract class MissionProgressDao {
    @Query("SELECT * FROM mission_progress WHERE storyId IN (:storyIds)")
    abstract fun observeProgress(storyIds: List<String>): Flow<List<MissionProgressEntity>>

    @Query("SELECT * FROM mission_progress WHERE storyId = :storyId")
    abstract fun observeProgress(storyId: String): Flow<MissionProgressEntity?>

    @Transaction
    @Query("SELECT * FROM story_sessions WHERE storyId = :storyId")
    abstract suspend fun loadSession(storyId: String): StorySessionWithAnswers?

    @Query("SELECT * FROM mission_progress WHERE storyId = :storyId")
    abstract suspend fun getProgress(storyId: String): MissionProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun upsertProgress(entity: MissionProgressEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun upsertSession(entity: StorySessionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun upsertAnswers(entities: List<StoryAnswerEntity>)

    @Query("DELETE FROM story_answers WHERE storyId = :storyId")
    protected abstract suspend fun deleteAnswers(storyId: String)

    @Query("DELETE FROM story_sessions WHERE storyId = :storyId")
    protected abstract suspend fun deleteSessionRow(storyId: String)

    @Query("DELETE FROM mission_progress WHERE storyId = :storyId")
    protected abstract suspend fun deleteProgress(storyId: String)

    @Query("DELETE FROM mission_progress")
    protected abstract suspend fun deleteAllProgressRows()

    @Query("DELETE FROM story_sessions")
    protected abstract suspend fun deleteAllSessions()

    @Query("DELETE FROM story_answers")
    protected abstract suspend fun deleteAllAnswers()

    @Transaction
    open suspend fun saveSession(
        storyId: String,
        currentStepIndex: Int,
        contentRevision: Int,
        selectedAnswers: Map<String, String>,
        nowEpochMillis: Long,
    ) {
        val existing = getProgress(storyId)
        upsertProgress(
            MissionProgressEntity(
                storyId = storyId,
                status = MissionProgressStatus.InProgress.name,
                startedAtEpochMillis = existing?.startedAtEpochMillis ?: nowEpochMillis,
                updatedAtEpochMillis = nowEpochMillis,
                completedAtEpochMillis = existing?.completedAtEpochMillis,
            ),
        )
        upsertSession(
            StorySessionEntity(
                storyId = storyId,
                currentStepIndex = currentStepIndex,
                contentRevision = contentRevision,
                sessionSchemaVersion = 1,
                updatedAtEpochMillis = nowEpochMillis,
            ),
        )
        deleteAnswers(storyId)
        upsertAnswers(
            selectedAnswers.map { (stepId, optionId) ->
                StoryAnswerEntity(
                    storyId = storyId,
                    stepId = stepId,
                    selectedOptionId = optionId,
                    updatedAtEpochMillis = nowEpochMillis,
                )
            },
        )
    }

    @Transaction
    open suspend fun markCompleted(
        storyId: String,
        nowEpochMillis: Long,
    ) {
        val existing = getProgress(storyId)
        upsertProgress(
            MissionProgressEntity(
                storyId = storyId,
                status = MissionProgressStatus.Completed.name,
                startedAtEpochMillis = existing?.startedAtEpochMillis ?: nowEpochMillis,
                updatedAtEpochMillis = nowEpochMillis,
                completedAtEpochMillis = nowEpochMillis,
            ),
        )
        deleteAnswers(storyId)
        deleteSessionRow(storyId)
    }

    @Transaction
    open suspend fun clearActiveSession(
        storyId: String,
        nowEpochMillis: Long,
    ) {
        val existing = getProgress(storyId)
        deleteSessionRow(storyId)
        if (existing?.completedAtEpochMillis == null) {
            deleteProgress(storyId)
        } else {
            upsertProgress(
                existing.copy(
                    status = MissionProgressStatus.Completed.name,
                    updatedAtEpochMillis = nowEpochMillis,
                ),
            )
        }
    }

    @Transaction
    open suspend fun clearAll() {
        deleteAllAnswers()
        deleteAllSessions()
        deleteAllProgressRows()
    }
}

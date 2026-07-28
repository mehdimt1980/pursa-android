package org.pursa.app.progress

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.pursa.app.journal.data.local.ReflectionJournalEntity
import org.pursa.app.progress.data.local.PursaDatabase
import org.pursa.app.progress.model.MissionProgressStatus

class MissionProgressDaoTest {
    private lateinit var database: PursaDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, PursaDatabase::class.java).build()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun savingAndReplacingActiveSessionStoresCurrentStepAndAnswers() = runBlocking {
        val dao = database.missionProgressDao()

        dao.saveSession(
            storyId = StoryId,
            currentStepIndex = 1,
            contentRevision = 1,
            selectedAnswers = mapOf("first_choice" to "tell_truth"),
            nowEpochMillis = 100L,
        )
        dao.saveSession(
            storyId = StoryId,
            currentStepIndex = 2,
            contentRevision = 1,
            selectedAnswers = mapOf(
                "first_choice" to "stay_quiet",
                "reason_focus" to "truth_telling",
            ),
            nowEpochMillis = 200L,
        )

        val session = dao.loadSession(StoryId)
        val progress = dao.observeProgress(StoryId).first()

        assertEquals(2, session?.session?.currentStepIndex)
        assertEquals(2, session?.answers?.size)
        assertEquals("stay_quiet", session?.answers?.first { it.stepId == "first_choice" }?.selectedOptionId)
        assertEquals(MissionProgressStatus.InProgress.name, progress?.status)
    }

    @Test
    fun completingStoryRemovesActiveSessionAndAnswersButKeepsCompletedStatus() = runBlocking {
        val dao = database.missionProgressDao()
        dao.saveSession(
            storyId = StoryId,
            currentStepIndex = 3,
            contentRevision = 1,
            selectedAnswers = mapOf("first_choice" to "tell_truth"),
            nowEpochMillis = 100L,
        )

        dao.markCompleted(StoryId, nowEpochMillis = 300L)

        val progress = dao.observeProgress(StoryId).first()
        assertEquals(MissionProgressStatus.Completed.name, progress?.status)
        assertEquals(300L, progress?.completedAtEpochMillis)
        assertNull(dao.loadSession(StoryId))
    }

    @Test
    fun replayCreatesActiveSessionWhileKeepingCompletionTimestamp() = runBlocking {
        val dao = database.missionProgressDao()
        dao.markCompleted(StoryId, nowEpochMillis = 300L)

        dao.saveSession(
            storyId = StoryId,
            currentStepIndex = 0,
            contentRevision = 1,
            selectedAnswers = emptyMap(),
            nowEpochMillis = 400L,
        )

        val progress = dao.observeProgress(StoryId).first()
        assertEquals(MissionProgressStatus.InProgress.name, progress?.status)
        assertEquals(300L, progress?.completedAtEpochMillis)
        assertEquals(0, dao.loadSession(StoryId)?.session?.currentStepIndex)
    }

    @Test
    fun clearAllDeletesProgressSessionsAndAnswers() = runBlocking {
        val dao = database.missionProgressDao()
        val journalDao = database.reflectionJournalDao()
        dao.saveSession(
            storyId = StoryId,
            currentStepIndex = 1,
            contentRevision = 1,
            selectedAnswers = mapOf("first_choice" to "tell_truth"),
            nowEpochMillis = 100L,
        )
        journalDao.upsertEntry(
            ReflectionJournalEntity(
                storyId = StoryId,
                contentRevision = 1,
                reflectionStepId = "final_reflection",
                selectedReflectionOptionId = "still_thinking",
                revisitQuestionStepId = "first_choice",
                completedAtEpochMillis = 100L,
                updatedAtEpochMillis = 100L,
                journalSchemaVersion = 1,
            ),
        )

        dao.clearAll()

        assertTrue(dao.observeProgress(listOf(StoryId)).first().isEmpty())
        assertNull(dao.loadSession(StoryId))
        assertEquals(0, journalDao.countEntries())
    }

    @Test
    fun differentStoriesDoNotOverwriteEachOther() = runBlocking {
        val dao = database.missionProgressDao()
        dao.saveSession(StoryId, 1, 1, mapOf("a" to "one"), 100L)
        dao.saveSession("truth_group_photo", 2, 1, mapOf("b" to "two"), 200L)

        assertEquals(1, dao.loadSession(StoryId)?.session?.currentStepIndex)
        assertEquals(2, dao.loadSession("truth_group_photo")?.session?.currentStepIndex)
    }

    private companion object {
        const val StoryId = "truth_broken_vase"
    }
}

package org.pursa.app.journal

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.pursa.app.journal.data.local.ReflectionJournalEntity
import org.pursa.app.progress.data.local.PursaDatabase

class ReflectionJournalDaoTest {
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
    fun upsertKeepsOneEntryPerStory() = runBlocking {
        val dao = database.reflectionJournalDao()

        dao.upsertEntry(entity(questionStepId = "first_question", updatedAt = 100L))
        dao.upsertEntry(entity(questionStepId = "second_question", updatedAt = 200L))

        val entry = dao.loadEntry(StoryId)
        assertEquals(1, dao.countEntries())
        assertEquals("second_question", entry?.revisitQuestionStepId)
        assertEquals(200L, entry?.updatedAtEpochMillis)
    }

    @Test
    fun entriesAreObservedNewestFirst() = runBlocking {
        val dao = database.reflectionJournalDao()
        dao.upsertEntry(entity(storyId = "older_story", updatedAt = 100L))
        dao.upsertEntry(entity(storyId = "newer_story", updatedAt = 300L))

        val entries = dao.observeEntries().first()

        assertEquals(listOf("newer_story", "older_story"), entries.map { it.storyId })
    }

    @Test
    fun deleteEntryRemovesOnlySelectedStory() = runBlocking {
        val dao = database.reflectionJournalDao()
        dao.upsertEntry(entity(storyId = StoryId))
        dao.upsertEntry(entity(storyId = "other_story"))

        dao.deleteEntry(StoryId)

        assertNull(dao.loadEntry(StoryId))
        assertEquals("other_story", dao.observeEntries().first().single().storyId)
    }

    private fun entity(
        storyId: String = StoryId,
        questionStepId: String = "question_step",
        updatedAt: Long = 100L,
    ) = ReflectionJournalEntity(
        storyId = storyId,
        contentRevision = 1,
        reflectionStepId = "final_reflection",
        selectedReflectionOptionId = "still_thinking",
        revisitQuestionStepId = questionStepId,
        completedAtEpochMillis = 50L,
        updatedAtEpochMillis = updatedAt,
        journalSchemaVersion = 1,
    )

    private companion object {
        const val StoryId = "truth_broken_vase"
    }
}

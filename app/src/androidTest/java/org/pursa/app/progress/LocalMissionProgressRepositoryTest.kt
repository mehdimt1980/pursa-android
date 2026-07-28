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
import org.pursa.app.content.data.JsonStoryParser
import org.pursa.app.content.data.StoryParseResult
import org.pursa.app.content.model.PursaStory
import org.pursa.app.content.state.StorySessionReducer
import org.pursa.app.content.state.StorySessionState
import org.pursa.app.core.time.PursaClock
import org.pursa.app.progress.data.LocalMissionProgressRepository
import org.pursa.app.progress.data.MissionProgressResult
import org.pursa.app.progress.data.local.PursaDatabase
import org.pursa.app.progress.model.MissionProgressStatus

class LocalMissionProgressRepositoryTest {
    private lateinit var database: PursaDatabase
    private lateinit var repository: LocalMissionProgressRepository
    private lateinit var story: PursaStory
    private val clock = FakeClock()

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, PursaDatabase::class.java).build()
        repository = LocalMissionProgressRepository(
            dao = database.missionProgressDao(),
            clock = clock,
        )
        story = (JsonStoryParser().parseStory(
            context.assets.open("content/fa/stories/truth/truth_broken_vase.json")
                .bufferedReader(Charsets.UTF_8)
                .use { it.readText() },
        ) as StoryParseResult.Success).value
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun noRecordsObserveAsNotStarted() = runBlocking {
        val progress = repository.observeProgress(StoryId).first()

        assertEquals(MissionProgressStatus.NotStarted, progress.status)
    }

    @Test
    fun savedSessionRestoresCurrentStepAndSelectedAnswers() = runBlocking {
        val state = StorySessionState(
            storyId = StoryId,
            currentStepIndex = 2,
            selectedAnswers = mapOf("first_choice" to "tell_truth"),
            completed = false,
        )

        repository.saveSessionSnapshot(story, state)
        val saved = (repository.loadSavedSession(story) as MissionProgressResult.Success).value

        assertEquals(2, saved?.currentStepIndex)
        assertEquals("tell_truth", saved?.selectedAnswers?.get("first_choice"))
        assertEquals(MissionProgressStatus.InProgress, repository.observeProgress(StoryId).first().status)
    }

    @Test
    fun completionPersistsAndDeletesActiveSession() = runBlocking {
        repository.saveSessionSnapshot(story, StorySessionReducer.initialState(story))
        repository.markCompleted(story)

        assertEquals(MissionProgressStatus.Completed, repository.observeProgress(StoryId).first().status)
        assertNull((repository.loadSavedSession(story) as MissionProgressResult.Success).value)
    }

    @Test
    fun replayPreservesCompletionTimestampWhileBecomingInProgress() = runBlocking {
        clock.now = 100L
        repository.markCompleted(story)
        clock.now = 200L
        repository.saveSessionSnapshot(story, StorySessionReducer.initialState(story))

        val progress = repository.observeProgress(StoryId).first()
        assertEquals(MissionProgressStatus.InProgress, progress.status)
        assertEquals(100L, progress.completedAtEpochMillis)
    }

    @Test
    fun clearAllReturnsStoryToNotStarted() = runBlocking {
        repository.saveSessionSnapshot(story, StorySessionReducer.initialState(story))
        repository.clearAllLocalProgress()

        assertEquals(MissionProgressStatus.NotStarted, repository.observeProgress(StoryId).first().status)
    }

    @Test
    fun contentRevisionMismatchClearsActiveSessionSafely() = runBlocking {
        repository.saveSessionSnapshot(story, StorySessionReducer.initialState(story))

        val changedStory = story.copy(contentRevision = story.contentRevision + 1)
        val saved = (repository.loadSavedSession(changedStory) as MissionProgressResult.Success).value

        assertNull(saved)
        assertEquals(MissionProgressStatus.NotStarted, repository.observeProgress(StoryId).first().status)
    }

    @Test
    fun invalidSavedAnswerIsIgnoredOnRestore() = runBlocking {
        val state = StorySessionState(
            storyId = StoryId,
            currentStepIndex = 2,
            selectedAnswers = mapOf(
                "first_choice" to "tell_truth",
                "missing_step" to "missing_option",
            ),
            completed = false,
        )

        repository.saveSessionSnapshot(story, state)
        val saved = (repository.loadSavedSession(story) as MissionProgressResult.Success).value

        assertEquals(mapOf("first_choice" to "tell_truth"), saved?.selectedAnswers)
    }

    private class FakeClock : PursaClock {
        var now: Long = 100L
        override fun nowEpochMillis(): Long = now
    }

    private companion object {
        const val StoryId = "truth_broken_vase"
    }
}

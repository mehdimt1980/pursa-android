package org.pursa.app.content.state

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.pursa.app.content.data.JsonStoryParser
import org.pursa.app.content.data.StoryParseResult
import org.pursa.app.content.model.PursaStory
import org.pursa.app.content.sampleStoryJson

class StorySessionReducerTest {
    @Test
    fun narrativeStepCanContinue() {
        assertTrue(StorySessionReducer.canContinue(story, StorySessionReducer.initialState(story)))
    }

    @Test
    fun requiredChoiceCannotContinueBeforeSelection() {
        val state = StorySessionReducer.advance(story, StorySessionReducer.initialState(story))

        assertFalse(StorySessionReducer.canContinue(story, state))
    }

    @Test
    fun selectionEnablesContinueAndCanBeReplaced() {
        val choiceState = StorySessionReducer.advance(story, StorySessionReducer.initialState(story))
        val selected = StorySessionReducer.selectAnswer(story, choiceState, "first_choice", "tell_truth")
        val replaced = StorySessionReducer.selectAnswer(story, selected, "first_choice", "stay_quiet")

        assertTrue(StorySessionReducer.canContinue(story, selected))
        assertEquals("stay_quiet", replaced.selectedAnswers["first_choice"])
    }

    @Test
    fun advancingUpdatesIndexAndFinalAdvanceCompletesSafely() {
        var state = StorySessionReducer.initialState(story)
        while (!state.completed) {
            val step = story.steps[state.currentStepIndex]
            state = StorySessionReducer.selectAnswer(story, state, step.id, firstSelectableOptionId(state.currentStepIndex))
            state = StorySessionReducer.advance(story, state)
        }

        assertTrue(state.completed)
        assertEquals(story.steps.lastIndex, state.currentStepIndex)
        assertEquals(state, StorySessionReducer.advance(story, state))
    }

    @Test
    fun answerStateSurvivesBackwardAndForwardTransitions() {
        val choiceState = StorySessionReducer.advance(story, StorySessionReducer.initialState(story))
        val selected = StorySessionReducer.selectAnswer(story, choiceState, "first_choice", "tell_truth")
        val next = StorySessionReducer.advance(story, selected)
        val previous = StorySessionReducer.previous(next)

        assertEquals("tell_truth", previous.selectedAnswers["first_choice"])
        assertTrue(StorySessionReducer.canContinue(story, previous))
    }

    @Test
    fun progressIsSafelyClamped() {
        val initial = StorySessionReducer.initialState(story)
        val completed = initial.copy(completed = true, currentStepIndex = 99)

        assertEquals(1f / story.steps.size.toFloat(), StorySessionReducer.progress(story, initial), 0f)
        assertEquals(1f, StorySessionReducer.progress(story, completed), 0f)
    }

    private fun firstSelectableOptionId(index: Int): String = when (index) {
        1 -> "tell_truth"
        2 -> "truth_telling"
        3 -> "trust_matters"
        4 -> "partly_changes"
        5 -> "partly_changed"
        else -> ""
    }

    private val story: PursaStory =
        (JsonStoryParser().parseStory(sampleStoryJson()) as StoryParseResult.Success).value
}

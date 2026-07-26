package org.pursa.app.content.state

import org.pursa.app.content.model.PursaStory
import org.pursa.app.content.model.PursaStoryStep

object StorySessionReducer {
    fun initialState(story: PursaStory): StorySessionState = StorySessionState(
        storyId = story.id,
        currentStepIndex = 0,
        selectedAnswers = emptyMap(),
        completed = false,
    )

    fun canContinue(
        story: PursaStory,
        state: StorySessionState,
    ): Boolean {
        if (state.completed) return false
        val step = story.steps.getOrNull(state.currentStepIndex) ?: return false
        return when (step) {
            is PursaStoryStep.Narrative -> true
            is PursaStoryStep.SingleChoice -> state.selectedAnswers[step.id] != null
            is PursaStoryStep.ReasonPrompt -> state.selectedAnswers[step.id] != null
            is PursaStoryStep.Perspective -> state.selectedAnswers[step.id] != null
            is PursaStoryStep.Counterexample -> state.selectedAnswers[step.id] != null
            is PursaStoryStep.Reflection -> state.selectedAnswers[step.id] != null
        }
    }

    fun selectAnswer(
        story: PursaStory,
        state: StorySessionState,
        stepId: String,
        optionId: String,
    ): StorySessionState {
        val step = story.steps.getOrNull(state.currentStepIndex) ?: return state
        if (step.id != stepId || step is PursaStoryStep.Narrative || state.completed) return state
        if (optionId !in step.optionIds()) return state
        return state.copy(
            selectedAnswers = state.selectedAnswers + (stepId to optionId),
        )
    }

    fun advance(
        story: PursaStory,
        state: StorySessionState,
    ): StorySessionState {
        if (!canContinue(story, state)) return state
        val lastIndex = story.steps.lastIndex
        return if (state.currentStepIndex >= lastIndex) {
            state.copy(completed = true)
        } else {
            state.copy(currentStepIndex = state.currentStepIndex + 1)
        }
    }

    fun previous(state: StorySessionState): StorySessionState {
        if (state.completed) return state.copy(completed = false)
        return state.copy(currentStepIndex = (state.currentStepIndex - 1).coerceAtLeast(0))
    }

    fun progress(
        story: PursaStory,
        state: StorySessionState,
    ): Float {
        if (story.steps.isEmpty()) return 0f
        val visibleStep = if (state.completed) story.steps.size else state.currentStepIndex + 1
        return (visibleStep.toFloat() / story.steps.size.toFloat()).coerceIn(0f, 1f)
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

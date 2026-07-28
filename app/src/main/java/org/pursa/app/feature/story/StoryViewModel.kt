package org.pursa.app.feature.story

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.pursa.app.content.data.StoryContentRepository
import org.pursa.app.content.data.StoryContentResult
import org.pursa.app.content.model.PursaStory
import org.pursa.app.content.state.StorySessionReducer
import org.pursa.app.content.state.StorySessionState
import org.pursa.app.progress.data.MissionProgressRepository
import org.pursa.app.progress.data.MissionProgressResult

class StoryViewModel(
    private val storyId: String,
    private val storyRepository: StoryContentRepository,
    private val progressRepository: MissionProgressRepository,
) : ViewModel() {
    private val _state = MutableStateFlow<StoryLoadState>(StoryLoadState.Loading)
    val state: StateFlow<StoryLoadState> = _state.asStateFlow()

    init {
        load()
    }

    fun retry() {
        load()
    }

    fun selectOption(optionId: String) {
        val success = _state.value as? StoryLoadState.Success ?: return
        val step = success.story.steps[success.sessionState.currentStepIndex]
        val next = StorySessionReducer.selectAnswer(
            story = success.story,
            state = success.sessionState,
            stepId = step.id,
            optionId = optionId,
        )
        updateSession(success.story, next)
    }

    fun advance() {
        val success = _state.value as? StoryLoadState.Success ?: return
        val next = StorySessionReducer.advance(success.story, success.sessionState)
        if (next.completed) {
            viewModelScope.launch {
                val result = progressRepository.markCompleted(success.story)
                _state.value = success.copy(
                    sessionState = next,
                    saveFailed = result is MissionProgressResult.Failure,
                )
            }
        } else {
            updateSession(success.story, next)
        }
    }

    fun previous() {
        val success = _state.value as? StoryLoadState.Success ?: return
        updateSession(success.story, StorySessionReducer.previous(success.sessionState))
    }

    private fun load() {
        _state.value = StoryLoadState.Loading
        viewModelScope.launch {
            when (val result = storyRepository.loadStory(storyId)) {
                is StoryContentResult.InvalidContent -> _state.value = StoryLoadState.InvalidContent
                StoryContentResult.NotFound -> _state.value = StoryLoadState.NotFound
                is StoryContentResult.ReadFailure -> _state.value = StoryLoadState.ReadFailure
                is StoryContentResult.Success -> restoreOrStart(result.value)
            }
        }
    }

    private suspend fun restoreOrStart(story: PursaStory) {
        val savedResult = progressRepository.loadSavedSession(story)
        val restored = (savedResult as? MissionProgressResult.Success)?.value
        val sessionState = if (restored == null) {
            StorySessionReducer.initialState(story)
        } else {
            StorySessionState(
                storyId = story.id,
                currentStepIndex = restored.currentStepIndex,
                selectedAnswers = restored.selectedAnswers,
                completed = false,
            )
        }
        val saveResult = progressRepository.saveSessionSnapshot(story, sessionState)
        _state.value = StoryLoadState.Success(
            story = story,
            sessionState = sessionState,
            saveFailed = saveResult is MissionProgressResult.Failure,
            restoredFromSavedSession = restored != null,
        )
    }

    private fun updateSession(
        story: PursaStory,
        sessionState: StorySessionState,
    ) {
        viewModelScope.launch {
            val result = progressRepository.saveSessionSnapshot(story, sessionState)
            _state.value = StoryLoadState.Success(
                story = story,
                sessionState = sessionState,
                saveFailed = result is MissionProgressResult.Failure,
                restoredFromSavedSession = false,
            )
        }
    }

    companion object {
        fun factory(
            storyId: String,
            storyRepository: StoryContentRepository,
            progressRepository: MissionProgressRepository,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T = StoryViewModel(
                storyId = storyId,
                storyRepository = storyRepository,
                progressRepository = progressRepository,
            ) as T
        }
    }
}

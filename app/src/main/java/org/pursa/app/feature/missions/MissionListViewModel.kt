package org.pursa.app.feature.missions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.pursa.app.content.data.StoryContentRepository
import org.pursa.app.content.data.StoryContentResult
import org.pursa.app.progress.data.MissionProgressRepository

class MissionListViewModel(
    private val worldId: String,
    private val storyRepository: StoryContentRepository,
    private val progressRepository: MissionProgressRepository,
) : ViewModel() {
    private val _state = MutableStateFlow<MissionListUiState>(MissionListUiState.Loading)
    val state: StateFlow<MissionListUiState> = _state.asStateFlow()

    private var progressJob: Job? = null

    init {
        load()
    }

    fun retry() {
        load()
    }

    private fun load() {
        progressJob?.cancel()
        _state.value = MissionListUiState.Loading
        viewModelScope.launch {
            when (val result = storyRepository.loadStoriesByWorld(worldId)) {
                is StoryContentResult.InvalidContent -> _state.value = MissionListUiState.InvalidContent
                StoryContentResult.NotFound -> _state.value = MissionListUiState.Success(emptyList())
                is StoryContentResult.ReadFailure -> _state.value = MissionListUiState.ReadFailure
                is StoryContentResult.Success -> {
                    val summaries = result.value
                    if (summaries.isEmpty()) {
                        _state.value = MissionListUiState.Success(emptyList())
                    } else {
                        progressJob = launch {
                            progressRepository.observeProgress(summaries.map { it.id }).collect { progressByStory ->
                                _state.value = MissionListUiState.Success(
                                    summaries.map { summary ->
                                        MissionListItemUiModel(
                                            summary = summary,
                                            status = progressByStory.getValue(summary.id).status,
                                        )
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        fun factory(
            worldId: String,
            storyRepository: StoryContentRepository,
            progressRepository: MissionProgressRepository,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T = MissionListViewModel(
                worldId = worldId,
                storyRepository = storyRepository,
                progressRepository = progressRepository,
            ) as T
        }
    }
}

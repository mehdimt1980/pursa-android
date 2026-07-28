package org.pursa.app.feature.journal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.pursa.app.content.data.StoryContentRepository
import org.pursa.app.content.data.StoryContentResult
import org.pursa.app.journal.data.ReflectionJournalRepository
import org.pursa.app.journal.model.ResolvedJournalEntry

class JournalListViewModel(
    private val journalRepository: ReflectionJournalRepository,
    private val storyRepository: StoryContentRepository,
) : ViewModel() {
    private val _state = MutableStateFlow<JournalListUiState>(JournalListUiState.Loading)
    val state: StateFlow<JournalListUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            journalRepository.observeEntries().collect { records ->
                val resolved = records.map { record ->
                    val story = (storyRepository.loadStory(record.storyId) as? StoryContentResult.Success)?.value
                    journalRepository.resolveEntry(record, story)
                }
                _state.value = JournalListUiState.Success(resolved)
            }
        }
    }

    companion object {
        fun factory(
            journalRepository: ReflectionJournalRepository,
            storyRepository: StoryContentRepository,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T = JournalListViewModel(
                journalRepository = journalRepository,
                storyRepository = storyRepository,
            ) as T
        }
    }
}

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
import org.pursa.app.journal.data.JournalResult
import org.pursa.app.journal.data.ReflectionJournalRepository

class JournalDetailViewModel(
    private val storyId: String,
    private val journalRepository: ReflectionJournalRepository,
    private val storyRepository: StoryContentRepository,
) : ViewModel() {
    private val _state = MutableStateFlow<JournalDetailUiState>(JournalDetailUiState.Loading)
    val state: StateFlow<JournalDetailUiState> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        _state.value = JournalDetailUiState.Loading
        viewModelScope.launch {
            val record = journalRepository.loadEntry(storyId)
            if (record == null) {
                _state.value = JournalDetailUiState.NotFound
                return@launch
            }
            val story = (storyRepository.loadStory(record.storyId) as? StoryContentResult.Success)?.value
            _state.value = JournalDetailUiState.Success(journalRepository.resolveEntry(record, story))
        }
    }

    fun showDeleteDialog() {
        val success = _state.value as? JournalDetailUiState.Success ?: return
        _state.value = success.copy(showDeleteDialog = true, deleteFailed = false)
    }

    fun dismissDeleteDialog() {
        val success = _state.value as? JournalDetailUiState.Success ?: return
        _state.value = success.copy(showDeleteDialog = false)
    }

    fun deleteEntry(onDeleted: () -> Unit) {
        viewModelScope.launch {
            val result = journalRepository.deleteEntry(storyId)
            if (result is JournalResult.Success) {
                onDeleted()
            } else {
                val success = _state.value as? JournalDetailUiState.Success
                if (success != null) {
                    _state.value = success.copy(showDeleteDialog = false, deleteFailed = true)
                }
            }
        }
    }

    companion object {
        fun factory(
            storyId: String,
            journalRepository: ReflectionJournalRepository,
            storyRepository: StoryContentRepository,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T = JournalDetailViewModel(
                storyId = storyId,
                journalRepository = journalRepository,
                storyRepository = storyRepository,
            ) as T
        }
    }
}

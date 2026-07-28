package org.pursa.app.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.pursa.app.progress.data.MissionProgressRepository
import org.pursa.app.progress.data.MissionProgressResult

class SettingsViewModel(
    private val progressRepository: MissionProgressRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    fun showDialog() {
        _state.value = _state.value.copy(
            showClearConfirmation = true,
            clearSucceeded = false,
            clearFailed = false,
        )
    }

    fun dismissDialog() {
        _state.value = _state.value.copy(showClearConfirmation = false)
    }

    fun clearProgress() {
        _state.value = _state.value.copy(clearInProgress = true)
        viewModelScope.launch {
            val result = progressRepository.clearAllLocalProgress()
            _state.value = SettingsUiState(
                clearSucceeded = result is MissionProgressResult.Success,
                clearFailed = result is MissionProgressResult.Failure,
            )
        }
    }

    companion object {
        fun factory(progressRepository: MissionProgressRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    SettingsViewModel(progressRepository) as T
            }
    }
}

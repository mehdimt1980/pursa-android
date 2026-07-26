package org.pursa.app.feature.missions

import org.pursa.app.content.model.PursaStorySummary

sealed interface MissionListUiState {
    data object Loading : MissionListUiState
    data class Success(val missions: List<PursaStorySummary>) : MissionListUiState
    data object InvalidContent : MissionListUiState
    data object ReadFailure : MissionListUiState
}

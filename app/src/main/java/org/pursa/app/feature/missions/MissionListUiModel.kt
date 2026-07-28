package org.pursa.app.feature.missions

import org.pursa.app.content.model.PursaStorySummary
import org.pursa.app.progress.model.MissionProgressStatus

data class MissionListItemUiModel(
    val summary: PursaStorySummary,
    val status: MissionProgressStatus,
)

sealed interface MissionListUiState {
    data object Loading : MissionListUiState
    data class Success(val missions: List<MissionListItemUiModel>) : MissionListUiState
    data object InvalidContent : MissionListUiState
    data object ReadFailure : MissionListUiState
}

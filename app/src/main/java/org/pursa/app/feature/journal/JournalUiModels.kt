package org.pursa.app.feature.journal

import org.pursa.app.journal.model.ResolvedJournalEntry

sealed interface JournalListUiState {
    data object Loading : JournalListUiState
    data class Success(val entries: List<ResolvedJournalEntry>) : JournalListUiState
    data object Error : JournalListUiState
}

sealed interface JournalDetailUiState {
    data object Loading : JournalDetailUiState
    data class Success(
        val entry: ResolvedJournalEntry,
        val showDeleteDialog: Boolean = false,
        val deleteFailed: Boolean = false,
    ) : JournalDetailUiState
    data object NotFound : JournalDetailUiState
    data object Error : JournalDetailUiState
}

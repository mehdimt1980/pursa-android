package org.pursa.app.feature.settings

data class SettingsUiState(
    val showClearConfirmation: Boolean = false,
    val clearInProgress: Boolean = false,
    val clearSucceeded: Boolean = false,
    val clearFailed: Boolean = false,
)

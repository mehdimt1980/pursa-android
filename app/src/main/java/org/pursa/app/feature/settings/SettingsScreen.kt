package org.pursa.app.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import org.pursa.app.R
import org.pursa.app.core.ui.PursaTestTags
import org.pursa.app.designsystem.component.PursaButton
import org.pursa.app.designsystem.component.PursaButtonVariant
import org.pursa.app.designsystem.component.PursaMessage
import org.pursa.app.designsystem.component.PursaMessageVariant
import org.pursa.app.designsystem.component.PursaTopBar
import org.pursa.app.designsystem.theme.PursaTheme

@Composable
fun SettingsScreen(
    state: SettingsUiState,
    onBackClick: () -> Unit,
    onShowClearDialog: () -> Unit,
    onDismissClearDialog: () -> Unit,
    onClearProgress: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxSize()
            .testTag(PursaTestTags.SettingsScreenRoot),
        color = PursaTheme.semanticColors.canvasWarm,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing),
        ) {
            PursaTopBar(
                title = stringResource(R.string.settings_title),
                navigationContentDescription = stringResource(R.string.navigation_back_content_description),
                onNavigationClick = onBackClick,
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        horizontal = PursaTheme.sizes.screenPadding,
                        vertical = PursaTheme.spacing.large,
                    )
                    .sizeIn(maxWidth = PursaTheme.sizes.contentMaxWidth),
                verticalArrangement = Arrangement.spacedBy(PursaTheme.spacing.large),
            ) {
                Text(
                    text = stringResource(R.string.settings_data_privacy_title),
                    modifier = Modifier.semantics { heading() },
                    color = PursaTheme.semanticColors.inkStrong,
                    style = MaterialTheme.typography.headlineMedium,
                )
                PursaMessage(
                    title = stringResource(R.string.settings_data_privacy_title),
                    message = stringResource(R.string.settings_data_privacy_message),
                )
                PursaButton(
                    text = stringResource(R.string.settings_clear_progress),
                    onClick = onShowClearDialog,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(PursaTestTags.SettingsClearProgress),
                    variant = PursaButtonVariant.Secondary,
                    fullWidth = true,
                )
                if (state.clearSucceeded) {
                    PursaMessage(
                        title = stringResource(R.string.settings_clear_success_title),
                        message = stringResource(R.string.settings_clear_success_message),
                        modifier = Modifier.testTag(PursaTestTags.SettingsClearSuccess),
                        variant = PursaMessageVariant.Success,
                    )
                }
                if (state.clearFailed) {
                    PursaMessage(
                        title = stringResource(R.string.settings_clear_failure_title),
                        message = stringResource(R.string.settings_clear_failure_message),
                        variant = PursaMessageVariant.Warning,
                    )
                }
            }
        }
    }

    if (state.showClearConfirmation) {
        AlertDialog(
            onDismissRequest = onDismissClearDialog,
            modifier = Modifier.testTag(PursaTestTags.SettingsClearDialog),
            title = {
                Text(text = stringResource(R.string.settings_clear_confirm_title))
            },
            text = {
                Text(text = stringResource(R.string.settings_clear_confirm_message))
            },
            confirmButton = {
                PursaButton(
                    text = stringResource(R.string.settings_clear_confirm_action),
                    onClick = onClearProgress,
                    modifier = Modifier.testTag(PursaTestTags.SettingsClearConfirm),
                    loading = state.clearInProgress,
                )
            },
            dismissButton = {
                PursaButton(
                    text = stringResource(R.string.settings_clear_cancel),
                    onClick = onDismissClearDialog,
                    modifier = Modifier.testTag(PursaTestTags.SettingsClearCancel),
                    variant = PursaButtonVariant.Secondary,
                )
            },
        )
    }
}

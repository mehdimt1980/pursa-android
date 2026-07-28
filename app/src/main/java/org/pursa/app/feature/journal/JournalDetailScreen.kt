package org.pursa.app.feature.journal

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
import org.pursa.app.designsystem.artwork.PursaArtwork
import org.pursa.app.designsystem.artwork.PursaArtworkRegistry
import org.pursa.app.designsystem.component.PursaButton
import org.pursa.app.designsystem.component.PursaButtonVariant
import org.pursa.app.designsystem.component.PursaCard
import org.pursa.app.designsystem.component.PursaMessage
import org.pursa.app.designsystem.component.PursaMessageVariant
import org.pursa.app.designsystem.component.PursaTopBar
import org.pursa.app.designsystem.theme.PursaTheme
import org.pursa.app.journal.model.ResolvedJournalEntry

@Composable
fun JournalDetailScreen(
    state: JournalDetailUiState,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onConfirmDelete: () -> Unit,
    onCancelDelete: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxSize()
            .testTag(PursaTestTags.JournalDetailRoot),
        color = PursaTheme.semanticColors.canvasWarm,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing),
        ) {
            PursaTopBar(
                title = stringResource(R.string.journal_title),
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
                JournalDetailContent(
                    state = state,
                    onDeleteClick = onDeleteClick,
                    onRetry = onRetry,
                )
            }
        }
    }

    val success = state as? JournalDetailUiState.Success
    if (success?.showDeleteDialog == true) {
        AlertDialog(
            onDismissRequest = onCancelDelete,
            modifier = Modifier.testTag(PursaTestTags.JournalDeleteDialog),
            title = { Text(text = stringResource(R.string.journal_delete_confirm_title)) },
            text = { Text(text = stringResource(R.string.journal_delete_confirm_message)) },
            confirmButton = {
                PursaButton(
                    text = stringResource(R.string.journal_delete_confirm),
                    onClick = onConfirmDelete,
                    modifier = Modifier.testTag(PursaTestTags.JournalDeleteConfirm),
                )
            },
            dismissButton = {
                PursaButton(
                    text = stringResource(R.string.journal_delete_cancel),
                    onClick = onCancelDelete,
                    modifier = Modifier.testTag(PursaTestTags.JournalDeleteCancel),
                    variant = PursaButtonVariant.Tertiary,
                )
            },
        )
    }
}

@Composable
private fun JournalDetailContent(
    state: JournalDetailUiState,
    onDeleteClick: () -> Unit,
    onRetry: () -> Unit,
) {
    when (state) {
        JournalDetailUiState.Loading -> PursaMessage(
            title = stringResource(R.string.journal_title),
            message = stringResource(R.string.missions_loading),
        )

        JournalDetailUiState.NotFound -> PursaMessage(
            title = stringResource(R.string.journal_entry_unavailable_title),
            message = stringResource(R.string.journal_entry_unavailable_message),
            variant = PursaMessageVariant.Empty,
        )

        JournalDetailUiState.Error -> PursaMessage(
            title = stringResource(R.string.story_read_failure_title),
            message = stringResource(R.string.story_read_failure_message),
            variant = PursaMessageVariant.Warning,
            action = {
                PursaButton(
                    text = stringResource(R.string.journal_retry),
                    onClick = onRetry,
                    variant = PursaButtonVariant.Secondary,
                )
            },
        )

        is JournalDetailUiState.Success -> {
            val title = when (val entry = state.entry) {
                is ResolvedJournalEntry.Available -> entry.storyTitle
                is ResolvedJournalEntry.Incompatible -> entry.storyTitle
                    ?: stringResource(R.string.journal_entry_unavailable_title)
                is ResolvedJournalEntry.StoryUnavailable -> stringResource(R.string.journal_entry_unavailable_title)
            }
            Text(
                text = title,
                modifier = Modifier.semantics { heading() },
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineMedium,
            )
            if (state.deleteFailed) {
                PursaMessage(
                    title = stringResource(R.string.settings_clear_failure_title),
                    message = stringResource(R.string.settings_clear_failure_message),
                    variant = PursaMessageVariant.Warning,
                )
            }
            when (val entry = state.entry) {
                is ResolvedJournalEntry.Available -> AvailableJournalDetail(entry)
                is ResolvedJournalEntry.Incompatible -> PursaMessage(
                    title = stringResource(R.string.journal_entry_unavailable_title),
                    message = stringResource(R.string.journal_entry_changed_message),
                    variant = PursaMessageVariant.Warning,
                )
                is ResolvedJournalEntry.StoryUnavailable -> PursaMessage(
                    title = stringResource(R.string.journal_entry_unavailable_title),
                    message = stringResource(R.string.journal_entry_unavailable_message),
                    variant = PursaMessageVariant.Warning,
                )
            }
            PursaButton(
                text = stringResource(R.string.journal_delete),
                onClick = onDeleteClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(PursaTestTags.JournalDeleteAction),
                variant = PursaButtonVariant.Secondary,
                fullWidth = true,
            )
        }
    }
}

@Composable
private fun AvailableJournalDetail(entry: ResolvedJournalEntry.Available) {
    PursaArtwork(
        descriptor = PursaArtworkRegistry.descriptorFor("story_${entry.record.storyId}"),
        modifier = Modifier
            .fillMaxWidth()
            .testTag(PursaTestTags.journalArtwork(entry.record.storyId)),
    )
    if (entry.contentChanged) {
        PursaMessage(
            title = stringResource(R.string.journal_entry_unavailable_title),
            message = stringResource(R.string.journal_entry_changed_message),
            variant = PursaMessageVariant.Warning,
        )
    }
    PursaCard(
        title = stringResource(R.string.journal_detail_question_heading),
        supportingText = entry.revisitQuestion,
        accentColor = PursaTheme.semanticColors.brand,
        containerColor = PursaTheme.semanticColors.reflectionContainer,
        contentColor = PursaTheme.semanticColors.onReflectionContainer,
        borderColor = PursaTheme.semanticColors.brand.copy(alpha = 0.34f),
        modifier = Modifier
            .fillMaxWidth()
            .testTag(PursaTestTags.JournalSavedQuestion),
    )
    entry.selectedReflectionLabel?.let {
        PursaCard(
            title = stringResource(R.string.journal_detail_reflection_heading),
            supportingText = it,
            containerColor = PursaTheme.semanticColors.readingSurface,
            borderColor = PursaTheme.semanticColors.outlineSoft,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(PursaTestTags.JournalReflectionSelection),
        )
    }
}

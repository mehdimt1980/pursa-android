package org.pursa.app.feature.journal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import java.text.DateFormat
import java.util.Date
import java.util.Locale

@Composable
fun JournalListScreen(
    state: JournalListUiState,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onEntryClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxSize()
            .testTag(PursaTestTags.JournalListRoot),
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
                Text(
                    text = stringResource(R.string.journal_title),
                    modifier = Modifier.semantics { heading() },
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.headlineMedium,
                )
                Text(
                    text = stringResource(R.string.journal_explanation),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )
                JournalListContent(
                    state = state,
                    onHomeClick = onHomeClick,
                    onEntryClick = onEntryClick,
                )
            }
        }
    }
}

@Composable
private fun JournalListContent(
    state: JournalListUiState,
    onHomeClick: () -> Unit,
    onEntryClick: (String) -> Unit,
) {
    when (state) {
        JournalListUiState.Loading -> PursaMessage(
            title = stringResource(R.string.journal_title),
            message = stringResource(R.string.missions_loading),
        )

        JournalListUiState.Error -> PursaMessage(
            title = stringResource(R.string.story_read_failure_title),
            message = stringResource(R.string.story_read_failure_message),
            variant = PursaMessageVariant.Warning,
        )

        is JournalListUiState.Success -> {
            if (state.entries.isEmpty()) {
                Column(
                    modifier = Modifier.testTag(PursaTestTags.JournalEmptyState),
                    verticalArrangement = Arrangement.spacedBy(PursaTheme.spacing.medium),
                ) {
                    PursaArtwork(
                        descriptor = PursaArtworkRegistry.descriptorFor("state_journal_empty"),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag(PursaTestTags.JournalEmptyArtwork),
                    )
                    PursaMessage(
                        title = stringResource(R.string.journal_empty_title),
                        message = stringResource(R.string.journal_empty_message),
                        variant = PursaMessageVariant.Empty,
                        action = {
                            PursaButton(
                                text = stringResource(R.string.journal_empty_action),
                                onClick = onHomeClick,
                                variant = PursaButtonVariant.Secondary,
                            )
                        },
                    )
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(PursaTheme.spacing.medium),
                ) {
                    state.entries.forEach { entry ->
                        PursaCard(
                            title = entryTitle(entry),
                            supportingText = entrySupportingText(entry),
                            onClick = { onEntryClick(entry.record.storyId) },
                            accentColor = PursaTheme.semanticColors.brand,
                            containerColor = PursaTheme.semanticColors.reflectionContainer,
                            contentColor = PursaTheme.semanticColors.onReflectionContainer,
                            borderColor = PursaTheme.semanticColors.brand.copy(alpha = 0.32f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag(PursaTestTags.journalEntry(entry.record.storyId)),
                            leadingContent = {
                                PursaArtwork(
                                    descriptor = PursaArtworkRegistry.descriptorFor(entry.artworkKey),
                                    modifier = Modifier
                                        .size(PursaTheme.sizes.welcomeMark)
                                        .testTag(PursaTestTags.journalArtwork(entry.record.storyId)),
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun entryTitle(entry: ResolvedJournalEntry): String = when (entry) {
    is ResolvedJournalEntry.Available -> entry.storyTitle
    is ResolvedJournalEntry.Incompatible -> entry.storyTitle ?: stringResource(R.string.journal_entry_unavailable_title)
    is ResolvedJournalEntry.StoryUnavailable -> stringResource(R.string.journal_entry_unavailable_title)
}

@Composable
private fun entrySupportingText(entry: ResolvedJournalEntry): String {
    val updatedAt = stringResource(
        R.string.journal_updated_at,
        formatJournalDate(entry.record.updatedAtEpochMillis),
    )
    val question = when (entry) {
        is ResolvedJournalEntry.Available -> entry.revisitQuestion
        is ResolvedJournalEntry.Incompatible -> stringResource(R.string.journal_entry_changed_message)
        is ResolvedJournalEntry.StoryUnavailable -> stringResource(R.string.journal_entry_unavailable_message)
    }
    return "$question\n$updatedAt"
}

private fun formatJournalDate(epochMillis: Long): String =
    DateFormat.getDateInstance(DateFormat.MEDIUM, Locale("fa"))
        .format(Date(epochMillis))

private val ResolvedJournalEntry.artworkKey: String
    get() = when (this) {
        is ResolvedJournalEntry.Available -> "story_${record.storyId}"
        is ResolvedJournalEntry.Incompatible -> "state_content_unavailable"
        is ResolvedJournalEntry.StoryUnavailable -> "state_content_unavailable"
    }

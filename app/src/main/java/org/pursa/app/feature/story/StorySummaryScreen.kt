package org.pursa.app.feature.story

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
import org.pursa.app.content.model.PursaStory
import org.pursa.app.core.ui.PursaTestTags
import org.pursa.app.designsystem.component.PursaButton
import org.pursa.app.designsystem.component.PursaButtonVariant
import org.pursa.app.designsystem.component.PursaCard
import org.pursa.app.designsystem.component.PursaMessage
import org.pursa.app.designsystem.component.PursaMessageVariant
import org.pursa.app.designsystem.theme.PursaTheme
import org.pursa.app.journal.data.finalReflectionStep
import org.pursa.app.journal.data.journalQuestionCandidates

@Composable
fun StorySummaryScreen(
    story: PursaStory,
    selectedAnswers: Map<String, String>,
    journalEntryExists: Boolean,
    selectedJournalQuestionStepId: String?,
    journalSaveFailed: Boolean,
    journalSaveSucceeded: Boolean,
    onReturnToWorld: () -> Unit,
    onSelectJournalQuestion: (String) -> Unit,
    onSaveJournalEntry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val journalQuestions = story.journalQuestionCandidates()
    val finalReflectionStep = story.finalReflectionStep()
    val finalReflectionLabel = finalReflectionStep
        ?.choices
        ?.firstOrNull { it.id == selectedAnswers[finalReflectionStep.id] }
        ?.label

    Surface(
        modifier = modifier
            .fillMaxSize()
            .testTag(PursaTestTags.StorySummaryRoot),
        color = PursaTheme.semanticColors.canvasWarm,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = PursaTheme.sizes.screenPadding,
                    vertical = PursaTheme.spacing.extraLarge,
                )
                .sizeIn(maxWidth = PursaTheme.sizes.contentMaxWidth),
            verticalArrangement = Arrangement.spacedBy(PursaTheme.spacing.large),
        ) {
            Text(
                text = story.title,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelLarge,
            )
            Text(
                text = story.completion.title,
                modifier = Modifier.semantics { heading() },
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineMedium,
            )
            PursaMessage(
                title = story.completion.title,
                message = story.completion.reflection,
                variant = PursaMessageVariant.Info,
            )
            if (selectedAnswers.isNotEmpty()) {
                PursaCard(
                    title = stringResource(R.string.story_progress_label, selectedAnswers.size, story.steps.size),
                    supportingText = story.completion.familyPrompt,
                    containerColor = PursaTheme.semanticColors.readingSurface,
                    borderColor = PursaTheme.semanticColors.outlineSoft,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            if (journalQuestions.isNotEmpty() && finalReflectionStep != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(PursaTestTags.SummaryJournalSection),
                    verticalArrangement = Arrangement.spacedBy(PursaTheme.spacing.medium),
                ) {
                    Text(
                        text = stringResource(R.string.summary_journal_title),
                        modifier = Modifier.semantics { heading() },
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Text(
                        text = stringResource(R.string.summary_journal_message),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    finalReflectionLabel?.let {
                        PursaCard(
                            title = stringResource(R.string.summary_journal_final_reflection),
                            supportingText = it,
                            containerColor = PursaTheme.semanticColors.reflectionContainer,
                            contentColor = PursaTheme.semanticColors.onReflectionContainer,
                            borderColor = PursaTheme.semanticColors.brand.copy(alpha = 0.34f),
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                    Text(
                        text = stringResource(R.string.summary_journal_question_heading),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    journalQuestions.forEach { candidate ->
                        val selected = candidate.stepId == selectedJournalQuestionStepId
                        PursaCard(
                            title = candidate.question,
                            supportingText = if (selected) {
                                stringResource(R.string.story_selected_option)
                            } else {
                                null
                            },
                            onClick = { onSelectJournalQuestion(candidate.stepId) },
                            accentColor = if (selected) PursaTheme.semanticColors.brand else null,
                            containerColor = if (selected) {
                                PursaTheme.semanticColors.brandContainer
                            } else {
                                PursaTheme.semanticColors.readingSurface
                            },
                            contentColor = if (selected) {
                                PursaTheme.semanticColors.onBrandContainer
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                            borderColor = if (selected) {
                                PursaTheme.semanticColors.brand
                            } else {
                                PursaTheme.semanticColors.outlineSoft
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag(PursaTestTags.summaryJournalQuestion(candidate.stepId)),
                        )
                    }
                    if (journalSaveSucceeded) {
                        PursaMessage(
                            title = stringResource(R.string.summary_journal_success_title),
                            message = stringResource(R.string.summary_journal_success_message),
                            modifier = Modifier.testTag(PursaTestTags.SummaryJournalSuccess),
                            variant = PursaMessageVariant.Success,
                        )
                    }
                    if (journalSaveFailed) {
                        PursaMessage(
                            title = stringResource(R.string.summary_journal_failure_title),
                            message = stringResource(R.string.summary_journal_failure_message),
                            variant = PursaMessageVariant.Warning,
                        )
                    }
                    PursaButton(
                        text = stringResource(
                            if (journalEntryExists) {
                                R.string.summary_journal_update
                            } else {
                                R.string.summary_journal_save
                            },
                        ),
                        onClick = onSaveJournalEntry,
                        enabled = selectedJournalQuestionStepId != null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag(PursaTestTags.SummaryJournalSave),
                        variant = PursaButtonVariant.Secondary,
                        fullWidth = true,
                    )
                }
            }
            PursaButton(
                text = stringResource(R.string.story_return_to_world),
                onClick = onReturnToWorld,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(PursaTestTags.StoryReturnToWorld),
                fullWidth = true,
            )
        }
    }
}

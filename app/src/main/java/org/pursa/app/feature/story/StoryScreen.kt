package org.pursa.app.feature.story

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import org.pursa.app.content.state.StorySessionReducer
import org.pursa.app.content.state.StorySessionState
import org.pursa.app.core.ui.PursaTestTags
import org.pursa.app.designsystem.component.PursaButton
import org.pursa.app.designsystem.component.PursaButtonVariant
import org.pursa.app.designsystem.component.PursaLinearProgress
import org.pursa.app.designsystem.component.PursaMessage
import org.pursa.app.designsystem.component.PursaMessageVariant
import org.pursa.app.designsystem.component.PursaTopBar
import org.pursa.app.designsystem.theme.PursaTheme

@Composable
fun StoryRouteScreen(
    state: StoryLoadState,
    onBackClick: () -> Unit,
    onRetry: () -> Unit,
    onReturnToWorld: () -> Unit,
    onSelectOption: (String) -> Unit,
    onAdvance: () -> Unit,
    onPrevious: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        StoryLoadState.Loading -> StoryStatusScreen(
            title = stringResource(R.string.story_loading),
            message = stringResource(R.string.story_loading),
            onBackClick = onBackClick,
            modifier = modifier,
        )
        StoryLoadState.NotFound -> StoryStatusScreen(
            title = stringResource(R.string.story_not_found_title),
            message = stringResource(R.string.story_not_found_message),
            onBackClick = onBackClick,
            modifier = modifier,
        )
        StoryLoadState.InvalidContent -> StoryStatusScreen(
            title = stringResource(R.string.story_invalid_title),
            message = stringResource(R.string.story_invalid_message),
            onBackClick = onBackClick,
            onRetry = onRetry,
            modifier = modifier,
        )
        StoryLoadState.ReadFailure -> StoryStatusScreen(
            title = stringResource(R.string.story_read_failure_title),
            message = stringResource(R.string.story_read_failure_message),
            onBackClick = onBackClick,
            onRetry = onRetry,
            modifier = modifier,
        )
        is StoryLoadState.Success -> StoryScreen(
            story = state.story,
            sessionState = state.sessionState,
            saveFailed = state.saveFailed,
            onBackClick = onBackClick,
            onReturnToWorld = onReturnToWorld,
            onSelectOption = onSelectOption,
            onAdvance = onAdvance,
            onPrevious = onPrevious,
            modifier = modifier,
        )
    }
}

@Composable
fun StoryScreen(
    story: PursaStory,
    sessionState: StorySessionState,
    saveFailed: Boolean,
    onBackClick: () -> Unit,
    onReturnToWorld: () -> Unit,
    onSelectOption: (String) -> Unit,
    onAdvance: () -> Unit,
    onPrevious: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (sessionState.completed) {
        StorySummaryScreen(
            story = story,
            selectedAnswers = sessionState.selectedAnswers,
            onReturnToWorld = onReturnToWorld,
            modifier = modifier,
        )
        return
    }

    StoryStepScaffold(
        story = story,
        sessionState = sessionState,
        saveFailed = saveFailed,
        onSelectOption = onSelectOption,
        onAdvance = onAdvance,
        onPrevious = onPrevious,
        onBackClick = onBackClick,
        modifier = modifier,
    )
}

@Composable
private fun StoryStepScaffold(
    story: PursaStory,
    sessionState: StorySessionState,
    saveFailed: Boolean,
    onSelectOption: (String) -> Unit,
    onAdvance: () -> Unit,
    onPrevious: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val step = story.steps[sessionState.currentStepIndex]
    Surface(
        modifier = modifier
            .fillMaxSize()
            .testTag(PursaTestTags.StoryScreenRoot),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing),
        ) {
            PursaTopBar(
                title = story.title,
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
                    text = story.title,
                    modifier = Modifier.semantics { heading() },
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.headlineMedium,
                )
                StoryProgress(
                    currentStep = sessionState.currentStepIndex + 1,
                    totalSteps = story.steps.size,
                    progress = StorySessionReducer.progress(story, sessionState),
                )
                StoryStepContent(
                    step = step,
                    selectedOptionId = sessionState.selectedAnswers[step.id],
                    onSelectOption = onSelectOption,
                )
                if (saveFailed) {
                    PursaMessage(
                        title = stringResource(R.string.story_save_failure_title),
                        message = stringResource(R.string.story_save_failure_message),
                        variant = PursaMessageVariant.Warning,
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(PursaTheme.spacing.medium),
                ) {
                    if (sessionState.currentStepIndex > 0) {
                        PursaButton(
                            text = stringResource(R.string.story_previous),
                            onClick = onPrevious,
                            modifier = Modifier
                                .weight(1f)
                                .testTag(PursaTestTags.StoryPrevious),
                            variant = PursaButtonVariant.Secondary,
                        )
                    }
                    PursaButton(
                        text = stringResource(R.string.story_continue),
                        onClick = onAdvance,
                        enabled = StorySessionReducer.canContinue(story, sessionState),
                        modifier = Modifier
                            .weight(1f)
                            .testTag(PursaTestTags.StoryContinue),
                    )
                }
            }
        }
    }
}

@Composable
private fun StoryProgress(
    currentStep: Int,
    totalSteps: Int,
    progress: Float,
) {
    Column(
        modifier = Modifier.testTag(PursaTestTags.StoryProgress),
        verticalArrangement = Arrangement.spacedBy(PursaTheme.spacing.small),
    ) {
        Text(
            text = stringResource(R.string.story_progress_label, currentStep, totalSteps),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelLarge,
        )
        PursaLinearProgress(progress = progress)
    }
}

@Composable
private fun StoryStatusScreen(
    title: String,
    message: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null,
) {
    Surface(
        modifier = modifier
            .fillMaxSize()
            .testTag(PursaTestTags.StoryErrorMessage),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing),
        ) {
            PursaTopBar(
                title = title,
                navigationContentDescription = stringResource(R.string.navigation_back_content_description),
                onNavigationClick = onBackClick,
            )
            PursaMessage(
                title = title,
                message = message,
                variant = PursaMessageVariant.Warning,
                modifier = Modifier
                    .padding(PursaTheme.sizes.screenPadding)
                    .fillMaxWidth(),
                action = onRetry?.let {
                    {
                        PursaButton(
                            text = stringResource(R.string.story_retry),
                            onClick = it,
                            variant = PursaButtonVariant.Secondary,
                        )
                    }
                },
            )
        }
    }
}

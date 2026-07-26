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
import org.pursa.app.designsystem.component.PursaCard
import org.pursa.app.designsystem.component.PursaMessage
import org.pursa.app.designsystem.theme.PursaTheme

@Composable
fun StorySummaryScreen(
    story: PursaStory,
    selectedAnswers: Map<String, String>,
    onReturnToWorld: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxSize()
            .testTag(PursaTestTags.StorySummaryRoot),
        color = MaterialTheme.colorScheme.background,
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
            )
            if (selectedAnswers.isNotEmpty()) {
                PursaCard(
                    title = stringResource(R.string.story_progress_label, selectedAnswers.size, story.steps.size),
                    supportingText = story.completion.familyPrompt,
                    modifier = Modifier.fillMaxWidth(),
                )
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

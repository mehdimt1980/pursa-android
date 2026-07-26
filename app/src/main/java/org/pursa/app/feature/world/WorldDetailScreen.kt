package org.pursa.app.feature.world

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import org.pursa.app.R
import org.pursa.app.core.ui.PursaTestTags
import org.pursa.app.designsystem.component.PursaCard
import org.pursa.app.designsystem.component.PursaMessage
import org.pursa.app.designsystem.component.PursaMessageVariant
import org.pursa.app.designsystem.component.PursaTopBar
import org.pursa.app.designsystem.theme.PursaTheme
import org.pursa.app.feature.home.PursaWorld
import org.pursa.app.feature.home.PursaWorldAccent
import org.pursa.app.feature.home.PursaWorlds
import org.pursa.app.feature.missions.MissionListScreen
import org.pursa.app.feature.missions.MissionListUiState

@Composable
fun WorldDetailScreen(
    world: PursaWorld,
    missionListState: MissionListUiState,
    onBackClick: () -> Unit,
    onMissionClick: (String) -> Unit,
    onRetryMissions: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxSize()
            .testTag(PursaTestTags.WorldDetailRoot),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing),
        ) {
            PursaTopBar(
                title = stringResource(world.titleResId),
                navigationContentDescription = stringResource(R.string.navigation_back_content_description),
                navigationModifier = Modifier.testTag(PursaTestTags.WorldDetailBack),
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
                PursaCard(
                    title = stringResource(world.titleResId),
                    supportingText = stringResource(world.detailResId),
                    accentColor = world.accent.accentColor(),
                    modifier = Modifier.fillMaxWidth(),
                )

                WorldQuestions(world = world)

                MissionListScreen(
                    state = missionListState,
                    onMissionClick = onMissionClick,
                    onRetry = onRetryMissions,
                )

                PursaMessage(
                    title = stringResource(R.string.world_detail_info_title),
                    message = stringResource(R.string.world_detail_info_message),
                    variant = PursaMessageVariant.Info,
                )
            }
        }
    }
}

@Composable
fun InvalidWorldScreen(
    onBackToHome: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxSize()
            .testTag(PursaTestTags.WorldDetailRoot),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing),
        ) {
            PursaTopBar(
                title = stringResource(R.string.world_not_found_title),
                navigationContentDescription = stringResource(R.string.navigation_back_content_description),
                navigationModifier = Modifier.testTag(PursaTestTags.WorldDetailBack),
                onNavigationClick = onBackToHome,
            )
            PursaMessage(
                title = stringResource(R.string.world_not_found_title),
                message = stringResource(R.string.world_not_found_message),
                variant = PursaMessageVariant.Warning,
                modifier = Modifier
                    .padding(PursaTheme.sizes.screenPadding)
                    .fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun WorldQuestions(world: PursaWorld) {
    Column(
        modifier = Modifier.testTag(PursaTestTags.WorldDetailQuestions),
        verticalArrangement = Arrangement.spacedBy(PursaTheme.spacing.medium),
    ) {
        Text(
            text = stringResource(R.string.world_detail_questions_heading),
            modifier = Modifier.semantics { heading() },
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
        )
        world.sampleQuestionResIds.forEach { questionResId ->
            PursaCard(
                title = stringResource(questionResId),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun PursaWorldAccent.accentColor(): Color = when (this) {
    PursaWorldAccent.Curiosity -> PursaTheme.semanticColors.curiosity
    PursaWorldAccent.Discovery -> PursaTheme.semanticColors.discovery
    PursaWorldAccent.Reflection -> PursaTheme.semanticColors.reflection
}

@Preview(name = "World detail", locale = "fa")
@Composable
private fun WorldDetailScreenPreview() {
    PursaTheme {
        WorldDetailScreen(
            world = PursaWorlds.all.first(),
            missionListState = MissionListUiState.Success(emptyList()),
            onBackClick = {},
            onMissionClick = {},
            onRetryMissions = {},
        )
    }
}

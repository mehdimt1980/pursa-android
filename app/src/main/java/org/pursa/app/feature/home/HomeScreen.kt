package org.pursa.app.feature.home

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
import org.pursa.app.designsystem.component.PursaButton
import org.pursa.app.designsystem.component.PursaButtonVariant
import org.pursa.app.designsystem.component.PursaMessage
import org.pursa.app.designsystem.component.PursaMessageVariant
import org.pursa.app.designsystem.theme.PursaTheme

@Composable
fun HomeScreen(
    worlds: List<PursaWorld>,
    onWorldClick: (String) -> Unit,
    onSettingsClick: () -> Unit,
    onJournalClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxSize()
            .testTag(PursaTestTags.HomeScreenRoot),
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
            HomeSectionHeader(
                title = stringResource(R.string.home_title),
                subtitle = stringResource(R.string.home_subtitle),
                onSettingsClick = onSettingsClick,
                onJournalClick = onJournalClick,
            )

            PursaMessage(
                title = stringResource(R.string.home_inquiry_title),
                message = stringResource(R.string.home_inquiry_message),
                variant = PursaMessageVariant.Info,
            )

            HomeWorldList(
                worlds = worlds,
                onWorldClick = onWorldClick,
            )
        }
    }
}

@Composable
private fun HomeSectionHeader(
    title: String,
    subtitle: String,
    onSettingsClick: () -> Unit,
    onJournalClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(PursaTheme.spacing.small),
    ) {
        Text(
            text = title,
            modifier = Modifier.semantics { heading() },
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = subtitle,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge,
        )
        PursaButton(
            text = stringResource(R.string.settings_title),
            onClick = onSettingsClick,
            variant = PursaButtonVariant.Tertiary,
        )
        PursaButton(
            text = stringResource(R.string.journal_title),
            onClick = onJournalClick,
            modifier = Modifier.testTag(PursaTestTags.HomeJournalAction),
            variant = PursaButtonVariant.Tertiary,
        )
    }
}

@Composable
private fun HomeWorldList(
    worlds: List<PursaWorld>,
    onWorldClick: (String) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(PursaTheme.spacing.medium),
    ) {
        Text(
            text = stringResource(R.string.home_worlds_heading),
            modifier = Modifier.semantics { heading() },
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
        )

        worlds.forEach { world ->
            PursaCard(
                title = stringResource(world.titleResId),
                supportingText = stringResource(world.summaryResId),
                onClick = { onWorldClick(world.id) },
                accentColor = world.accent.accentColor(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(world.testTag),
            )
        }
    }
}

private val PursaWorld.testTag: String
    get() = when (id) {
        PursaWorlds.TruthId -> PursaTestTags.HomeWorldTruth
        PursaWorlds.JusticeId -> PursaTestTags.HomeWorldJustice
        PursaWorlds.FriendshipId -> PursaTestTags.HomeWorldFriendship
        else -> "pursa:home:world:$id"
    }

@Composable
private fun PursaWorldAccent.accentColor(): Color = when (this) {
    PursaWorldAccent.Curiosity -> PursaTheme.semanticColors.curiosity
    PursaWorldAccent.Discovery -> PursaTheme.semanticColors.discovery
    PursaWorldAccent.Reflection -> PursaTheme.semanticColors.reflection
}

@Preview(name = "Home", locale = "fa")
@Composable
private fun HomeScreenPreview() {
    PursaTheme {
        HomeScreen(
            worlds = PursaWorlds.all,
            onWorldClick = {},
            onSettingsClick = {},
            onJournalClick = {},
        )
    }
}

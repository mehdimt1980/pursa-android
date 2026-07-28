package org.pursa.app.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.tooling.preview.Preview
import org.pursa.app.R
import org.pursa.app.core.ui.PursaTestTags
import org.pursa.app.designsystem.component.PursaButton
import org.pursa.app.designsystem.component.PursaButtonVariant
import org.pursa.app.designsystem.component.PursaBackgroundPattern
import org.pursa.app.designsystem.component.PursaCard
import org.pursa.app.designsystem.component.PursaWorldArtwork
import org.pursa.app.designsystem.theme.PursaTheme
import org.pursa.app.designsystem.theme.pursaWorldStyle

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
        color = PursaTheme.semanticColors.canvasWarm,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            PursaBackgroundPattern(modifier = Modifier.fillMaxSize())
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        horizontal = PursaTheme.sizes.screenPadding,
                        vertical = PursaTheme.spacing.extraLarge,
                    )
                    .sizeIn(maxWidth = PursaTheme.sizes.contentMaxWidth),
                verticalArrangement = Arrangement.spacedBy(PursaTheme.spacing.large),
            ) {
                HomeHero(onSettingsClick = onSettingsClick)

                PursaCard(
                    title = stringResource(R.string.home_inquiry_title),
                    supportingText = stringResource(R.string.home_inquiry_message),
                    containerColor = PursaTheme.semanticColors.readingSurface,
                    borderColor = PursaTheme.semanticColors.outlineSoft,
                    modifier = Modifier.fillMaxWidth(),
                )

                HomeWorldList(
                    worlds = worlds,
                    onWorldClick = onWorldClick,
                )

                JournalEntryPoint(onJournalClick = onJournalClick)
            }
        }
    }
}

@Composable
private fun HomeHero(
    onSettingsClick: () -> Unit,
) {
    PursaCard(
        title = stringResource(R.string.home_title),
        supportingText = stringResource(R.string.home_subtitle),
        containerColor = PursaTheme.semanticColors.brandContainer,
        contentColor = PursaTheme.semanticColors.onBrandContainer,
        borderColor = PursaTheme.semanticColors.brand.copy(alpha = 0.28f),
        modifier = Modifier.fillMaxWidth(),
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(PursaTheme.sizes.welcomeMark)
                    .background(PursaTheme.semanticColors.brand.copy(alpha = 0.14f), CircleShape),
            )
        },
        trailingContent = {
            PursaButton(
                text = stringResource(R.string.settings_title),
                onClick = onSettingsClick,
                variant = PursaButtonVariant.Tertiary,
            )
        },
    )
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
            val style = pursaWorldStyle(world.id)
            PursaCard(
                title = stringResource(world.titleResId),
                supportingText = stringResource(world.summaryResId),
                onClick = { onWorldClick(world.id) },
                accentColor = style.accent,
                containerColor = style.soft,
                contentColor = style.onContainer,
                borderColor = style.accent.copy(alpha = 0.34f),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(world.testTag),
                trailingContent = {
                    PursaWorldArtwork(style = style)
                },
            )
        }
    }
}

@Composable
private fun JournalEntryPoint(onJournalClick: () -> Unit) {
    PursaCard(
        title = stringResource(R.string.journal_title),
        supportingText = stringResource(R.string.journal_explanation),
        onClick = onJournalClick,
        accentColor = PursaTheme.semanticColors.brand,
        containerColor = PursaTheme.semanticColors.reflectionContainer,
        contentColor = PursaTheme.semanticColors.onReflectionContainer,
        borderColor = PursaTheme.semanticColors.brand.copy(alpha = 0.34f),
        modifier = Modifier
            .fillMaxWidth()
            .testTag(PursaTestTags.HomeJournalAction),
        leadingContent = {
            Row(horizontalArrangement = Arrangement.spacedBy(PursaTheme.spacing.extraSmall)) {
                Box(
                    modifier = Modifier
                        .size(PursaTheme.sizes.iconMedium)
                        .background(PursaTheme.semanticColors.brand.copy(alpha = 0.28f), CircleShape),
                )
                Box(
                    modifier = Modifier
                        .size(PursaTheme.sizes.iconSmall)
                        .background(PursaTheme.semanticColors.brandContainer, CircleShape),
                )
            }
        },
    )
}

private val PursaWorld.testTag: String
    get() = when (id) {
        PursaWorlds.TruthId -> PursaTestTags.HomeWorldTruth
        PursaWorlds.JusticeId -> PursaTestTags.HomeWorldJustice
        PursaWorlds.FriendshipId -> PursaTestTags.HomeWorldFriendship
        else -> "pursa:home:world:$id"
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

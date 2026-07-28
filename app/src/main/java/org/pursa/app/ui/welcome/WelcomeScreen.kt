package org.pursa.app.ui.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import org.pursa.app.R
import org.pursa.app.core.ui.PursaTestTags
import org.pursa.app.designsystem.component.PursaBackgroundPattern
import org.pursa.app.designsystem.component.PursaButton
import org.pursa.app.designsystem.component.PursaCard
import org.pursa.app.designsystem.theme.PursaTheme

@Composable
fun WelcomeScreen(
    onPrimaryAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxSize()
            .testTag(PursaTestTags.WelcomeScreenRoot),
        color = PursaTheme.semanticColors.canvasWarm,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = PursaTheme.sizes.screenPadding,
                    vertical = PursaTheme.spacing.extraLarge,
                ),
            contentAlignment = Alignment.Center,
        ) {
            PursaBackgroundPattern(modifier = Modifier.fillMaxSize())
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .sizeIn(maxWidth = PursaTheme.sizes.contentMaxWidth),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(R.string.welcome_early_stage_label),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Start,
                )

                Spacer(modifier = Modifier.height(PursaTheme.spacing.large))

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(PursaTheme.semanticColors.brandContainer)
                        .sizeIn(
                            minWidth = PursaTheme.sizes.welcomeMark,
                            minHeight = PursaTheme.sizes.welcomeMark,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(R.string.app_name),
                        color = PursaTheme.semanticColors.onBrandContainer,
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                    )
                }

                Spacer(modifier = Modifier.height(PursaTheme.spacing.extraLarge))

                Text(
                    text = stringResource(R.string.app_name),
                    modifier = Modifier.testTag(PursaTestTags.WelcomeAppName),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.displaySmall,
                    textAlign = TextAlign.Start,
                )

                Spacer(modifier = Modifier.height(PursaTheme.spacing.small))

                Text(
                    text = stringResource(R.string.welcome_tagline),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Start,
                )

                Spacer(modifier = Modifier.height(PursaTheme.spacing.medium))

                Text(
                    text = stringResource(R.string.welcome_description),
                    color = PursaTheme.semanticColors.inkDefault,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Start,
                )

                Spacer(modifier = Modifier.height(PursaTheme.spacing.large))

                PursaCard(
                    title = stringResource(R.string.home_inquiry_title),
                    supportingText = stringResource(R.string.home_inquiry_message),
                    containerColor = PursaTheme.semanticColors.readingSurface,
                    borderColor = PursaTheme.semanticColors.outlineSoft,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(PursaTheme.spacing.extraLarge))

                PursaButton(
                    text = stringResource(R.string.welcome_primary_action),
                    onClick = onPrimaryAction,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(PursaTestTags.WelcomePrimaryAction),
                    fullWidth = true,
                )
            }
        }
    }
}

@Preview(name = "Welcome")
@Composable
private fun WelcomeScreenPreview() {
    PursaTheme {
        WelcomeScreen(onPrimaryAction = {})
    }
}

@Preview(name = "Compact", widthDp = 320, heightDp = 640)
@Composable
private fun WelcomeScreenCompactPreview() {
    PursaTheme {
        WelcomeScreen(onPrimaryAction = {})
    }
}

@Preview(name = "RTL Persian", locale = "fa", widthDp = 360, heightDp = 720)
@Composable
private fun WelcomeScreenRtlPersianPreview() {
    PursaTheme {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            WelcomeScreen(onPrimaryAction = {})
        }
    }
}

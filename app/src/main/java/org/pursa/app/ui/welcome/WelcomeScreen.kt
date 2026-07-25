package org.pursa.app.ui.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import org.pursa.app.R
import org.pursa.app.core.ui.PursaTestTags
import org.pursa.app.ui.theme.PursaTheme

private val WelcomeContentPadding = PaddingValues(horizontal = 28.dp, vertical = 32.dp)
private val WelcomeContentMaxWidth = 560.dp
private val WelcomeButtonMinHeight = 48.dp
private val WelcomeMarkSize = 72.dp

@Composable
fun WelcomeScreen(
    onPrimaryAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxSize()
            .testTag(PursaTestTags.WelcomeScreenRoot),
        color = MaterialTheme.colorScheme.background,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .verticalScroll(rememberScrollState())
                .padding(WelcomeContentPadding),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .sizeIn(maxWidth = WelcomeContentMaxWidth),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .sizeIn(
                            minWidth = WelcomeMarkSize,
                            minHeight = WelcomeMarkSize,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(R.string.app_name),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = stringResource(R.string.app_name),
                    modifier = Modifier.testTag(PursaTestTags.WelcomeAppName),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.displayMedium,
                    textAlign = TextAlign.Start,
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = stringResource(R.string.welcome_tagline),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Start,
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = stringResource(R.string.welcome_description),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Start,
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onPrimaryAction,
                    modifier = Modifier
                        .fillMaxWidth()
                        .sizeIn(minHeight = WelcomeButtonMinHeight)
                        .testTag(PursaTestTags.WelcomePrimaryAction),
                ) {
                    Text(text = stringResource(R.string.welcome_primary_action))
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = stringResource(R.string.welcome_early_stage_label),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Start,
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

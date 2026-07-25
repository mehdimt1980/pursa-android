package org.pursa.app.designsystem.preview

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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import org.pursa.app.designsystem.component.PursaButton
import org.pursa.app.designsystem.component.PursaButtonVariant
import org.pursa.app.designsystem.component.PursaCard
import org.pursa.app.designsystem.component.PursaLabelChip
import org.pursa.app.designsystem.component.PursaLinearProgress
import org.pursa.app.designsystem.component.PursaMessage
import org.pursa.app.designsystem.component.PursaMessageVariant
import org.pursa.app.designsystem.component.PursaSelectableChip
import org.pursa.app.designsystem.component.PursaStepIndicator
import org.pursa.app.designsystem.component.PursaTopBar
import org.pursa.app.designsystem.theme.PursaTheme

@Composable
fun PursaDesignSystemShowcase(
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .verticalScroll(rememberScrollState())
                .padding(PursaTheme.spacing.large),
            verticalArrangement = Arrangement.spacedBy(PursaTheme.spacing.large),
        ) {
            PursaTopBar(
                title = PursaPreviewData.topBarTitle,
                navigationContentDescription = PursaPreviewData.backDescription,
                onNavigationClick = {},
            )

            ShowcaseSection(title = "رنگ‌ها") {
                Column(
                    verticalArrangement = Arrangement.spacedBy(PursaTheme.spacing.small),
                ) {
                    ColorSwatch("Primary", MaterialTheme.colorScheme.primary)
                    ColorSwatch("Curiosity", PursaTheme.semanticColors.curiosity)
                    ColorSwatch("Discovery", PursaTheme.semanticColors.discovery)
                    ColorSwatch("Success", PursaTheme.semanticColors.success)
                    ColorSwatch("Warning", PursaTheme.semanticColors.warning)
                }
            }

            ShowcaseSection(title = "نوشتار") {
                Text(
                    text = PursaPreviewData.appName,
                    style = MaterialTheme.typography.displaySmall,
                )
                Text(
                    text = PursaPreviewData.tagline,
                    style = MaterialTheme.typography.headlineMedium,
                )
                Text(
                    text = PursaPreviewData.body,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = PursaPreviewData.mixedText,
                    style = MaterialTheme.typography.labelLarge,
                )
            }

            ShowcaseSection(title = "دکمه‌ها") {
                PursaButton(
                    text = PursaPreviewData.primaryAction,
                    onClick = {},
                    fullWidth = true,
                )
                PursaButton(
                    text = PursaPreviewData.secondaryAction,
                    onClick = {},
                    variant = PursaButtonVariant.Secondary,
                    fullWidth = true,
                )
                PursaButton(
                    text = PursaPreviewData.tertiaryAction,
                    onClick = {},
                    variant = PursaButtonVariant.Tertiary,
                )
                PursaButton(
                    text = PursaPreviewData.loadingAction,
                    onClick = {},
                    loading = true,
                )
            }

            ShowcaseSection(title = "کارت‌ها و برچسب‌ها") {
                PursaCard(
                    title = PursaPreviewData.cardTitle,
                    supportingText = PursaPreviewData.cardText,
                    onClick = {},
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(PursaTheme.spacing.small),
                ) {
                    PursaLabelChip(text = PursaPreviewData.topicLabel)
                    PursaLabelChip(text = PursaPreviewData.ageLabel)
                    PursaSelectableChip(
                        text = PursaPreviewData.choiceLabel,
                        selected = true,
                        onSelectedChange = {},
                    )
                }
            }

            ShowcaseSection(title = "پیشرفت") {
                PursaLinearProgress(progress = 0.42f)
                PursaStepIndicator(currentStep = 2, totalSteps = 5)
            }

            ShowcaseSection(title = "پیام‌ها") {
                PursaMessage(
                    title = PursaPreviewData.messageTitle,
                    message = PursaPreviewData.messageText,
                )
                PursaMessage(
                    title = PursaPreviewData.successTitle,
                    message = PursaPreviewData.messageText,
                    variant = PursaMessageVariant.Success,
                )
                PursaMessage(
                    title = PursaPreviewData.warningTitle,
                    message = PursaPreviewData.messageText,
                    variant = PursaMessageVariant.Warning,
                )
                PursaMessage(
                    title = PursaPreviewData.errorTitle,
                    message = PursaPreviewData.messageText,
                    variant = PursaMessageVariant.Error,
                )
                PursaMessage(
                    title = PursaPreviewData.emptyTitle,
                    message = PursaPreviewData.messageText,
                    variant = PursaMessageVariant.Empty,
                )
            }
        }
    }
}

@Composable
private fun ShowcaseSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = PursaTheme.sizes.contentMaxWidth),
        verticalArrangement = Arrangement.spacedBy(PursaTheme.spacing.medium),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
        )
        content()
    }
}

@Composable
private fun ColorSwatch(
    label: String,
    color: androidx.compose.ui.graphics.Color,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(PursaTheme.spacing.small),
    ) {
        Box(
            modifier = Modifier
                .size(PursaTheme.sizes.iconMedium)
                .background(color, MaterialTheme.shapes.small),
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
        )
    }
}

@Preview(name = "Design system", locale = "fa", widthDp = 390, heightDp = 900)
@Composable
private fun PursaDesignSystemShowcasePreview() {
    PursaTheme {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            PursaDesignSystemShowcase()
        }
    }
}

@Preview(name = "Compact", locale = "fa", widthDp = 320, heightDp = 720)
@Composable
private fun PursaDesignSystemShowcaseCompactPreview() {
    PursaTheme {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            PursaDesignSystemShowcase()
        }
    }
}

@Preview(name = "Large font", locale = "fa", fontScale = 1.3f, widthDp = 390, heightDp = 900)
@Composable
private fun PursaDesignSystemShowcaseLargeFontPreview() {
    PursaTheme {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            PursaDesignSystemShowcase()
        }
    }
}

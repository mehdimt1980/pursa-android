package org.pursa.app.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.pursa.app.core.ui.PursaTestTags
import org.pursa.app.designsystem.preview.PursaPreviewData
import org.pursa.app.designsystem.theme.PursaTheme

enum class PursaMessageVariant {
    Info,
    Success,
    Warning,
    Error,
    Empty,
}

@Composable
fun PursaMessage(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    variant: PursaMessageVariant = PursaMessageVariant.Info,
    action: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
) {
    val semanticColors = PursaTheme.semanticColors
    val (containerColor, contentColor) = when (variant) {
        PursaMessageVariant.Info -> semanticColors.infoContainer to semanticColors.onInfoContainer
        PursaMessageVariant.Success -> semanticColors.successContainer to semanticColors.onSuccessContainer
        PursaMessageVariant.Warning -> semanticColors.warningContainer to semanticColors.onWarningContainer
        PursaMessageVariant.Error -> MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.onErrorContainer
        PursaMessageVariant.Empty -> semanticColors.canvasSecondary to semanticColors.inkDefault
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = containerColor,
        contentColor = contentColor,
        shape = MaterialTheme.shapes.large,
        tonalElevation = 1.dp,
    ) {
        Row(
            modifier = Modifier.padding(PursaTheme.spacing.medium),
            horizontalArrangement = Arrangement.spacedBy(PursaTheme.spacing.medium),
            verticalAlignment = Alignment.Top,
        ) {
            icon?.invoke()
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(PursaTheme.spacing.extraSmall),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                )
                action?.invoke()
            }
        }
    }
}

@Preview(name = "Pursa message", locale = "fa")
@Composable
private fun PursaMessagePreview() {
    PursaTheme {
        PursaMessage(
            title = PursaPreviewData.messageTitle,
            message = PursaPreviewData.messageText,
            modifier = Modifier.testTag(PursaTestTags.DesignSystemMessage),
            variant = PursaMessageVariant.Info,
        )
    }
}

package org.pursa.app.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import org.pursa.app.core.ui.PursaTestTags
import org.pursa.app.designsystem.preview.PursaPreviewData
import org.pursa.app.designsystem.theme.PursaTheme

enum class PursaButtonVariant {
    Primary,
    Secondary,
    Tertiary,
}

@Composable
fun PursaButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: PursaButtonVariant = PursaButtonVariant.Primary,
    enabled: Boolean = true,
    loading: Boolean = false,
    fullWidth: Boolean = false,
    leadingIcon: (@Composable () -> Unit)? = null,
) {
    val buttonModifier = modifier
        .then(if (fullWidth) Modifier.fillMaxWidth() else Modifier)
        .sizeIn(minHeight = PursaTheme.sizes.minimumTouchTarget)
        .semantics { role = Role.Button }
    val contentPadding = PaddingValues(
        horizontal = PursaTheme.spacing.large,
        vertical = PursaTheme.spacing.small,
    )
    val effectiveEnabled = enabled && !loading
    val content: @Composable () -> Unit = {
        ButtonContent(
            text = text,
            loading = loading,
            leadingIcon = leadingIcon,
        )
    }

    when (variant) {
        PursaButtonVariant.Primary -> Button(
            onClick = onClick,
            modifier = buttonModifier,
            enabled = effectiveEnabled,
            contentPadding = contentPadding,
            content = { content() },
        )

        PursaButtonVariant.Secondary -> OutlinedButton(
            onClick = onClick,
            modifier = buttonModifier,
            enabled = effectiveEnabled,
            contentPadding = contentPadding,
            content = { content() },
        )

        PursaButtonVariant.Tertiary -> TextButton(
            onClick = onClick,
            modifier = buttonModifier,
            enabled = effectiveEnabled,
            contentPadding = contentPadding,
            content = { content() },
        )
    }
}

@Composable
private fun ButtonContent(
    text: String,
    loading: Boolean,
    leadingIcon: (@Composable () -> Unit)?,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier.size(PursaTheme.sizes.iconMedium),
            contentAlignment = Alignment.Center,
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(PursaTheme.sizes.iconSmall),
                    strokeWidth = ButtonDefaults.IconSpacing,
                    color = LocalContentColor.current,
                )
            } else {
                leadingIcon?.invoke()
            }
        }
        Spacer(modifier = Modifier.width(PursaTheme.spacing.small))
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

@Preview(name = "Pursa buttons", locale = "fa")
@Composable
private fun PursaButtonPreview() {
    PursaTheme {
        androidx.compose.foundation.layout.Column {
            PursaButton(
                text = PursaPreviewData.primaryAction,
                onClick = {},
                modifier = Modifier.testTag(PursaTestTags.DesignSystemPrimaryButton),
            )
            PursaButton(
                text = PursaPreviewData.secondaryAction,
                onClick = {},
                variant = PursaButtonVariant.Secondary,
            )
            PursaButton(
                text = PursaPreviewData.loadingAction,
                onClick = {},
                loading = true,
            )
        }
    }
}

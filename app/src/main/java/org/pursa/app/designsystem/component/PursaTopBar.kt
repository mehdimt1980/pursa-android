package org.pursa.app.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import org.pursa.app.designsystem.preview.PursaPreviewData
import org.pursa.app.designsystem.theme.PursaTheme

@Composable
fun PursaTopBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationContentDescription: String? = null,
    navigationModifier: Modifier = Modifier,
    onNavigationClick: (() -> Unit)? = null,
    actions: (@Composable () -> Unit)? = null,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = PursaTheme.sizes.topBarHeight),
            horizontalArrangement = Arrangement.spacedBy(PursaTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (onNavigationClick != null && navigationContentDescription != null) {
                TextButton(
                    onClick = onNavigationClick,
                    modifier = navigationModifier
                        .sizeIn(
                            minWidth = PursaTheme.sizes.minimumTouchTarget,
                            minHeight = PursaTheme.sizes.minimumTouchTarget,
                        )
                        .semantics { contentDescription = navigationContentDescription },
                ) {
                    Text(text = "‹")
                }
            } else {
                Spacer(modifier = Modifier.width(PursaTheme.spacing.medium))
            }

            Text(
                text = title,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleLarge,
            )
            Box(contentAlignment = Alignment.Center) {
                actions?.invoke()
            }
        }
    }
}

@Preview(name = "Pursa top bar", locale = "fa")
@Composable
private fun PursaTopBarPreview() {
    PursaTheme {
        PursaTopBar(
            title = PursaPreviewData.topBarTitle,
            navigationContentDescription = PursaPreviewData.backDescription,
            onNavigationClick = {},
        )
    }
}

package org.pursa.app.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import org.pursa.app.designsystem.preview.PursaPreviewData
import org.pursa.app.designsystem.theme.PursaTheme

@Composable
fun PursaCard(
    title: String,
    modifier: Modifier = Modifier,
    supportingText: String? = null,
    onClick: (() -> Unit)? = null,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
) {
    val interactiveModifier = if (onClick != null) {
        Modifier
            .sizeIn(minHeight = PursaTheme.sizes.minimumTouchTarget)
            .clickable(onClick = onClick)
            .semantics { role = Role.Button }
    } else {
        Modifier
    }

    Card(
        modifier = modifier.then(interactiveModifier),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = PursaTheme.spacing.extraSmall),
        shape = MaterialTheme.shapes.large,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PursaTheme.spacing.medium),
            horizontalArrangement = Arrangement.spacedBy(PursaTheme.spacing.medium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            leadingContent?.invoke()
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(PursaTheme.spacing.extraSmall),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                )
                supportingText?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            trailingContent?.invoke()
        }
    }
}

@Preview(name = "Pursa card", locale = "fa")
@Composable
private fun PursaCardPreview() {
    PursaTheme {
        PursaCard(
            title = PursaPreviewData.cardTitle,
            supportingText = PursaPreviewData.cardText,
            onClick = {},
        )
    }
}

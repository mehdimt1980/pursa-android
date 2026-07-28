package org.pursa.app.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.pursa.app.designsystem.preview.PursaPreviewData
import org.pursa.app.designsystem.theme.PursaTheme

@Composable
fun PursaCard(
    title: String,
    modifier: Modifier = Modifier,
    supportingText: String? = null,
    onClick: (() -> Unit)? = null,
    accentColor: Color? = null,
    containerColor: Color? = null,
    contentColor: Color? = null,
    borderColor: Color? = null,
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

    val resolvedContainerColor = containerColor ?: MaterialTheme.colorScheme.surface
    val resolvedContentColor = contentColor ?: MaterialTheme.colorScheme.onSurface
    val resolvedBorderColor = borderColor ?: PursaTheme.semanticColors.outlineSoft

    Card(
        modifier = modifier.then(interactiveModifier),
        colors = CardDefaults.cardColors(
            containerColor = resolvedContainerColor,
            contentColor = resolvedContentColor,
        ),
        border = BorderStroke(1.dp, resolvedBorderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = MaterialTheme.shapes.large,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PursaTheme.spacing.medium),
            horizontalArrangement = Arrangement.spacedBy(PursaTheme.spacing.medium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            accentColor?.let {
                Box(
                    modifier = Modifier
                        .width(PursaTheme.spacing.extraSmall)
                        .heightIn(min = PursaTheme.sizes.minimumTouchTarget)
                        .background(it, MaterialTheme.shapes.extraSmall),
                )
            }
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
                        color = resolvedContentColor.copy(alpha = 0.82f),
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

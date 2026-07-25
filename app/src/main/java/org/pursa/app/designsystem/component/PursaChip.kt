package org.pursa.app.designsystem.component

import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import org.pursa.app.core.ui.PursaTestTags
import org.pursa.app.designsystem.preview.PursaPreviewData
import org.pursa.app.designsystem.theme.PursaTheme

@Composable
fun PursaLabelChip(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Surface(
        modifier = modifier.sizeIn(minHeight = PursaTheme.sizes.minimumTouchTarget),
        color = if (enabled) {
            MaterialTheme.colorScheme.secondaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        },
        contentColor = if (enabled) {
            MaterialTheme.colorScheme.onSecondaryContainer
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        shape = MaterialTheme.shapes.small,
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(
                horizontal = PursaTheme.spacing.medium,
                vertical = PursaTheme.spacing.small,
            ),
            style = MaterialTheme.typography.labelMedium,
        )
    }
}

@Composable
fun PursaSelectableChip(
    text: String,
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    FilterChip(
        selected = selected,
        onClick = { onSelectedChange(!selected) },
        label = {
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
            )
        },
        modifier = modifier.semantics { role = Role.Checkbox },
        enabled = enabled,
    )
}

@Preview(name = "Pursa chips", locale = "fa")
@Composable
private fun PursaChipPreview() {
    PursaTheme {
        androidx.compose.foundation.layout.Row {
            PursaLabelChip(text = PursaPreviewData.topicLabel)
            PursaSelectableChip(
                text = PursaPreviewData.choiceLabel,
                selected = true,
                onSelectedChange = {},
                modifier = Modifier.testTag(PursaTestTags.DesignSystemSelectableChip),
            )
        }
    }
}

package org.pursa.app.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import org.pursa.app.core.ui.PursaTestTags
import org.pursa.app.designsystem.preview.PursaPreviewData
import org.pursa.app.designsystem.theme.PursaTheme

fun coerceProgress(progress: Float): Float = progress.coerceIn(0f, 1f)

fun progressFromSteps(currentStep: Int, totalSteps: Int): Float {
    if (totalSteps <= 0) return 0f
    return (currentStep.toFloat() / totalSteps.toFloat()).coerceIn(0f, 1f)
}

@Composable
fun PursaLinearProgress(
    progress: Float,
    modifier: Modifier = Modifier,
) {
    val coercedProgress = coerceProgress(progress)
    LinearProgressIndicator(
        progress = { coercedProgress },
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                progressBarRangeInfo = ProgressBarRangeInfo(
                    current = coercedProgress,
                    range = 0f..1f,
                    steps = 0,
                )
            },
        color = MaterialTheme.colorScheme.tertiary,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
    )
}

@Composable
fun PursaStepIndicator(
    currentStep: Int,
    totalSteps: Int,
    modifier: Modifier = Modifier,
) {
    val safeTotal = totalSteps.coerceAtLeast(0)
    val safeCurrent = currentStep.coerceIn(0, safeTotal)
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(PursaTheme.spacing.small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(safeTotal) { index ->
            val isCompleted = index < safeCurrent
            Surface(
                modifier = Modifier.size(PursaTheme.sizes.iconSmall),
                color = if (isCompleted) {
                    MaterialTheme.colorScheme.tertiary
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                },
                contentColor = if (isCompleted) {
                    MaterialTheme.colorScheme.onTertiary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                shape = MaterialTheme.shapes.extraSmall,
            ) {
                Text(
                    text = if (isCompleted) "✓" else "",
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}

@Preview(name = "Pursa progress", locale = "fa")
@Composable
private fun PursaProgressPreview() {
    PursaTheme {
        androidx.compose.foundation.layout.Column(
            verticalArrangement = Arrangement.spacedBy(PursaTheme.spacing.medium),
        ) {
            Text(PursaPreviewData.progressLabel)
            PursaLinearProgress(
                progress = 0.42f,
                modifier = Modifier.testTag(PursaTestTags.DesignSystemProgress),
            )
            PursaStepIndicator(currentStep = 2, totalSteps = 5)
        }
    }
}

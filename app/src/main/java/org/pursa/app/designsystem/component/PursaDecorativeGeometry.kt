package org.pursa.app.designsystem.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
import org.pursa.app.designsystem.theme.PursaTheme
import org.pursa.app.designsystem.theme.PursaWorldMotif
import org.pursa.app.designsystem.theme.PursaWorldStyle

@Composable
fun PursaDecorativeCluster(
    style: PursaWorldStyle,
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier.clearAndSetSemantics {},
    ) {
        val accent = style.accent.copy(alpha = 0.32f)
        val container = style.container.copy(alpha = 0.72f)
        val softLine = style.accent.copy(alpha = 0.48f)
        when (style.motif) {
            PursaWorldMotif.Truth -> {
                drawCircle(container, radius = size.minDimension * 0.28f, center = Offset(size.width * 0.38f, size.height * 0.42f))
                drawCircle(accent, radius = size.minDimension * 0.24f, center = Offset(size.width * 0.62f, size.height * 0.44f), style = Stroke(width = 5.dp.toPx()))
                val points = listOf(
                    Offset(size.width * 0.24f, size.height * 0.72f),
                    Offset(size.width * 0.48f, size.height * 0.62f),
                    Offset(size.width * 0.72f, size.height * 0.76f),
                )
                drawLine(softLine, points[0], points[1], strokeWidth = 3.dp.toPx(), cap = StrokeCap.Round)
                drawLine(softLine, points[1], points[2], strokeWidth = 3.dp.toPx(), cap = StrokeCap.Round)
                points.forEach { drawCircle(style.accent, radius = 4.dp.toPx(), center = it) }
            }
            PursaWorldMotif.Justice -> {
                drawRoundRect(container, topLeft = Offset(size.width * 0.14f, size.height * 0.58f), size = Size(size.width * 0.72f, size.height * 0.16f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(12.dp.toPx()))
                drawRoundRect(accent, topLeft = Offset(size.width * 0.18f, size.height * 0.34f), size = Size(size.width * 0.24f, size.height * 0.22f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(10.dp.toPx()))
                drawRoundRect(style.accent.copy(alpha = 0.42f), topLeft = Offset(size.width * 0.58f, size.height * 0.26f), size = Size(size.width * 0.24f, size.height * 0.30f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(10.dp.toPx()))
                drawLine(softLine, Offset(size.width * 0.5f, size.height * 0.2f), Offset(size.width * 0.5f, size.height * 0.8f), strokeWidth = 3.dp.toPx(), cap = StrokeCap.Round)
            }
            PursaWorldMotif.Friendship -> {
                drawCircle(container, radius = size.minDimension * 0.18f, center = Offset(size.width * 0.34f, size.height * 0.42f))
                drawCircle(style.accent.copy(alpha = 0.36f), radius = size.minDimension * 0.18f, center = Offset(size.width * 0.56f, size.height * 0.42f))
                val path = Path().apply {
                    moveTo(size.width * 0.22f, size.height * 0.72f)
                    quadraticBezierTo(size.width * 0.5f, size.height * 0.56f, size.width * 0.78f, size.height * 0.72f)
                }
                drawPath(path, softLine, style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round))
            }
            PursaWorldMotif.Reflection -> {
                drawCircle(container, radius = size.minDimension * 0.24f, center = Offset(size.width * 0.45f, size.height * 0.42f))
                drawCircle(accent, radius = size.minDimension * 0.16f, center = Offset(size.width * 0.64f, size.height * 0.58f))
                drawArc(softLine, startAngle = 205f, sweepAngle = 250f, useCenter = false, topLeft = Offset(size.width * 0.18f, size.height * 0.18f), size = Size(size.width * 0.62f, size.height * 0.62f), style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round))
            }
        }
    }
}

@Composable
fun PursaBackgroundPattern(
    modifier: Modifier = Modifier,
) {
    val colors = PursaTheme.semanticColors
    Box(modifier = modifier.clearAndSetSemantics {}) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(colors.geometrySoft.copy(alpha = 0.34f), radius = size.minDimension * 0.34f, center = Offset(size.width * 0.04f, size.height * 0.08f))
            drawCircle(colors.geometryWarm.copy(alpha = 0.34f), radius = size.minDimension * 0.24f, center = Offset(size.width * 0.92f, size.height * 0.16f))
            drawCircle(colors.truthContainer.copy(alpha = 0.28f), radius = size.minDimension * 0.18f, center = Offset(size.width * 0.82f, size.height * 0.86f))
        }
    }
}

@Composable
fun PursaWorldArtwork(
    style: PursaWorldStyle,
    modifier: Modifier = Modifier,
) {
    PursaDecorativeCluster(
        style = style,
        modifier = modifier.size(112.dp),
    )
}

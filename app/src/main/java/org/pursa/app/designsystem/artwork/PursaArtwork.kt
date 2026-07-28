package org.pursa.app.designsystem.artwork

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.matchParentSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import org.pursa.app.designsystem.theme.PursaTheme
import org.pursa.app.designsystem.theme.pursaWorldStyle

@Composable
fun PursaArtwork(
    descriptor: PursaArtworkDescriptor,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    val worldStyle = pursaWorldStyle(descriptor.worldId.orEmpty())
    val semanticColors = PursaTheme.semanticColors
    val semanticsModifier = when (descriptor.accessibility) {
        PursaArtworkAccessibility.Decorative -> Modifier.clearAndSetSemantics {}
        PursaArtworkAccessibility.Informative -> Modifier.semantics {
            this.contentDescription = contentDescription.orEmpty()
        }
    }

    Box(
        modifier = Modifier
            .then(semanticsModifier)
            .then(modifier)
            .aspectRatio(descriptor.aspectRatio)
            .clip(MaterialTheme.shapes.extraLarge)
            .background(worldStyle.soft),
    ) {
        Canvas(
            modifier = Modifier
                .matchParentSize()
                .padding(8.dp),
        ) {
            val palette = ArtworkPalette(
                accent = worldStyle.accent,
                container = worldStyle.container,
                ink = semanticColors.inkStrong,
                brand = semanticColors.brand,
                warm = semanticColors.canvasSecondary,
                soft = worldStyle.soft,
            )
            drawArtworkScene(descriptor.scene, palette)
        }
    }
}

private data class ArtworkPalette(
    val accent: Color,
    val container: Color,
    val ink: Color,
    val brand: Color,
    val warm: Color,
    val soft: Color,
)

private fun DrawScope.drawArtworkScene(
    scene: PursaArtworkScene,
    palette: ArtworkPalette,
) {
    drawBackground(palette)
    when (scene) {
        PursaArtworkScene.TruthWorld -> drawTruthWorld(palette)
        PursaArtworkScene.JusticeWorld -> drawJusticeWorld(palette)
        PursaArtworkScene.FriendshipWorld -> drawFriendshipWorld(palette)
        PursaArtworkScene.BrokenVase -> drawBrokenVase(palette)
        PursaArtworkScene.GroupPhoto -> drawGroupPhoto(palette)
        PursaArtworkScene.StrangeNews -> drawStrangeNews(palette)
        PursaArtworkScene.FriendSecret -> drawFriendSecret(palette)
        PursaArtworkScene.LastCake -> drawLastCake(palette)
        PursaArtworkScene.ClassRepresentative -> drawClassRepresentative(palette)
        PursaArtworkScene.PlaygroundRule -> drawPlaygroundRule(palette)
        PursaArtworkScene.TeamPrize -> drawTeamPrize(palette)
        PursaArtworkScene.NewFriend -> drawNewFriend(palette)
        PursaArtworkScene.DifficultPromise -> drawDifficultPromise(palette)
        PursaArtworkScene.WhoseSide -> drawWhoseSide(palette)
        PursaArtworkScene.GameWithoutThem -> drawGameWithoutThem(palette)
        PursaArtworkScene.StoryComplete -> drawStoryComplete(palette)
        PursaArtworkScene.JournalEmpty -> drawJournalEmpty(palette)
        PursaArtworkScene.Fallback -> drawFallback(palette)
    }
}

private fun DrawScope.drawBackground(palette: ArtworkPalette) {
    drawCircle(palette.container.copy(alpha = 0.72f), size.minDimension * 0.32f, Offset(size.width * 0.18f, size.height * 0.18f))
    drawCircle(palette.warm.copy(alpha = 0.78f), size.minDimension * 0.26f, Offset(size.width * 0.86f, size.height * 0.24f))
    drawCircle(palette.accent.copy(alpha = 0.12f), size.minDimension * 0.28f, Offset(size.width * 0.72f, size.height * 0.82f))
}

private fun DrawScope.drawTruthWorld(palette: ArtworkPalette) {
    drawRing(palette.accent, 0.42f, 0.44f, 0.18f)
    drawRing(palette.brand, 0.58f, 0.44f, 0.14f)
    drawConnectedPoints(palette)
}

private fun DrawScope.drawJusticeWorld(palette: ArtworkPalette) {
    drawBalancedBlocks(palette, leftHeight = 0.22f, rightHeight = 0.34f)
}

private fun DrawScope.drawFriendshipWorld(palette: ArtworkPalette) {
    drawLinkedForms(palette, separated = false)
}

private fun DrawScope.drawBrokenVase(palette: ArtworkPalette) {
    drawCircle(palette.accent.copy(alpha = 0.7f), size.minDimension * 0.08f, Offset(size.width * 0.28f, size.height * 0.28f))
    drawPot(palette, broken = true)
    drawFigure(palette, 0.72f, 0.58f)
}

private fun DrawScope.drawGroupPhoto(palette: ArtworkPalette) {
    drawFrame(palette, 0.18f, 0.22f, 0.58f, 0.5f)
    repeat(3) { index -> drawFigure(palette, 0.34f + index * 0.14f, 0.48f) }
    drawLine(palette.accent, Offset(size.width * 0.2f, size.height * 0.7f), Offset(size.width * 0.78f, size.height * 0.58f), 3.dp.toPx(), StrokeCap.Round)
}

private fun DrawScope.drawStrangeNews(palette: ArtworkPalette) {
    val source = Offset(size.width * 0.22f, size.height * 0.5f)
    drawCircle(palette.accent, 7.dp.toPx(), source)
    listOf(0.42f to 0.28f, 0.54f to 0.5f, 0.44f to 0.72f, 0.72f to 0.38f, 0.72f to 0.66f).forEach { (x, y) ->
        val target = Offset(size.width * x, size.height * y)
        drawLine(palette.accent.copy(alpha = 0.55f), source, target, 2.dp.toPx(), StrokeCap.Round)
        drawMessageCard(palette, x, y)
    }
}

private fun DrawScope.drawFriendSecret(palette: ArtworkPalette) {
    drawFigure(palette, 0.32f, 0.56f)
    drawFigure(palette, 0.66f, 0.56f)
    drawRoundRect(palette.container, Offset(size.width * 0.44f, size.height * 0.36f), Size(size.width * 0.14f, size.height * 0.1f), CornerRadius(8.dp.toPx()))
}

private fun DrawScope.drawLastCake(palette: ArtworkPalette) {
    drawCircle(palette.container, size.minDimension * 0.2f, Offset(size.width * 0.48f, size.height * 0.46f))
    drawArc(palette.accent, 300f, 55f, true, Offset(size.width * 0.34f, size.height * 0.28f), Size(size.width * 0.28f, size.width * 0.28f))
    drawBalancedBlocks(palette, 0.14f, 0.14f)
}

private fun DrawScope.drawClassRepresentative(palette: ArtworkPalette) {
    repeat(4) { index -> drawFigure(palette, 0.24f + index * 0.16f, 0.62f) }
    drawRoundRect(palette.accent.copy(alpha = 0.42f), Offset(size.width * 0.38f, size.height * 0.24f), Size(size.width * 0.26f, size.height * 0.16f), CornerRadius(14.dp.toPx()))
}

private fun DrawScope.drawPlaygroundRule(palette: ArtworkPalette) {
    drawRoundRect(palette.container, Offset(size.width * 0.18f, size.height * 0.58f), Size(size.width * 0.64f, size.height * 0.1f), CornerRadius(18.dp.toPx()))
    drawRoundRect(palette.accent.copy(alpha = 0.48f), Offset(size.width * 0.54f, size.height * 0.34f), Size(size.width * 0.18f, size.height * 0.28f), CornerRadius(12.dp.toPx()))
    drawFigure(palette, 0.3f, 0.5f)
}

private fun DrawScope.drawTeamPrize(palette: ArtworkPalette) {
    drawBalancedBlocks(palette, 0.28f, 0.2f)
    repeat(3) { index -> drawFigure(palette, 0.32f + index * 0.16f, 0.68f) }
}

private fun DrawScope.drawNewFriend(palette: ArtworkPalette) {
    drawLinkedForms(palette, separated = true)
    drawFigure(palette, 0.3f, 0.58f)
    drawFigure(palette, 0.7f, 0.58f)
}

private fun DrawScope.drawDifficultPromise(palette: ArtworkPalette) {
    drawLinkedForms(palette, separated = false)
    drawRoundRect(palette.container, Offset(size.width * 0.42f, size.height * 0.26f), Size(size.width * 0.18f, size.height * 0.14f), CornerRadius(10.dp.toPx()))
}

private fun DrawScope.drawWhoseSide(palette: ArtworkPalette) {
    drawFigure(palette, 0.25f, 0.58f)
    drawFigure(palette, 0.75f, 0.58f)
    drawFigure(palette, 0.5f, 0.42f)
    drawPath(Path().apply {
        moveTo(size.width * 0.34f, size.height * 0.58f)
        quadraticTo(size.width * 0.5f, size.height * 0.48f, size.width * 0.66f, size.height * 0.58f)
    }, palette.accent, style = Stroke(3.dp.toPx(), cap = StrokeCap.Round))
}

private fun DrawScope.drawGameWithoutThem(palette: ArtworkPalette) {
    drawRoundRect(palette.container, Offset(size.width * 0.24f, size.height * 0.42f), Size(size.width * 0.44f, size.height * 0.18f), CornerRadius(16.dp.toPx()))
    drawFigure(palette, 0.28f, 0.7f)
    drawFigure(palette, 0.48f, 0.7f)
    drawFigure(palette, 0.78f, 0.5f)
}

private fun DrawScope.drawStoryComplete(palette: ArtworkPalette) {
    drawRing(palette.brand, 0.5f, 0.5f, 0.2f)
    drawConnectedPoints(palette)
}

private fun DrawScope.drawJournalEmpty(palette: ArtworkPalette) {
    drawRoundRect(palette.container, Offset(size.width * 0.3f, size.height * 0.28f), Size(size.width * 0.4f, size.height * 0.44f), CornerRadius(20.dp.toPx()))
    drawLine(palette.accent, Offset(size.width * 0.38f, size.height * 0.42f), Offset(size.width * 0.62f, size.height * 0.42f), 3.dp.toPx(), StrokeCap.Round)
    drawLine(palette.accent.copy(alpha = 0.62f), Offset(size.width * 0.38f, size.height * 0.52f), Offset(size.width * 0.56f, size.height * 0.52f), 3.dp.toPx(), StrokeCap.Round)
}

private fun DrawScope.drawFallback(palette: ArtworkPalette) {
    drawRing(palette.brand, 0.5f, 0.5f, 0.18f)
}

private fun DrawScope.drawFigure(palette: ArtworkPalette, x: Float, y: Float) {
    drawCircle(palette.brand.copy(alpha = 0.72f), size.minDimension * 0.045f, Offset(size.width * x, size.height * (y - 0.12f)))
    drawRoundRect(palette.accent.copy(alpha = 0.42f), Offset(size.width * (x - 0.04f), size.height * (y - 0.06f)), Size(size.width * 0.08f, size.height * 0.16f), CornerRadius(18.dp.toPx()))
}

private fun DrawScope.drawPot(palette: ArtworkPalette, broken: Boolean) {
    drawRoundRect(palette.container, Offset(size.width * 0.42f, size.height * 0.5f), Size(size.width * 0.18f, size.height * 0.18f), CornerRadius(12.dp.toPx()))
    if (broken) {
        drawLine(palette.ink.copy(alpha = 0.5f), Offset(size.width * 0.48f, size.height * 0.5f), Offset(size.width * 0.54f, size.height * 0.68f), 2.dp.toPx())
        drawCircle(palette.container, size.minDimension * 0.035f, Offset(size.width * 0.38f, size.height * 0.68f))
        drawCircle(palette.container, size.minDimension * 0.028f, Offset(size.width * 0.64f, size.height * 0.66f))
    }
}

private fun DrawScope.drawFrame(palette: ArtworkPalette, x: Float, y: Float, width: Float, height: Float) {
    drawRoundRect(palette.warm, Offset(size.width * x, size.height * y), Size(size.width * width, size.height * height), CornerRadius(18.dp.toPx()))
    drawRoundRect(palette.accent, Offset(size.width * x, size.height * y), Size(size.width * width, size.height * height), CornerRadius(18.dp.toPx()), style = Stroke(3.dp.toPx()))
}

private fun DrawScope.drawMessageCard(palette: ArtworkPalette, x: Float, y: Float) {
    drawRoundRect(palette.warm, Offset(size.width * (x - 0.055f), size.height * (y - 0.035f)), Size(size.width * 0.11f, size.height * 0.07f), CornerRadius(8.dp.toPx()))
    drawCircle(palette.accent.copy(alpha = 0.58f), 3.dp.toPx(), Offset(size.width * x, size.height * y))
}

private fun DrawScope.drawRing(paletteColor: Color, x: Float, y: Float, radius: Float) {
    drawCircle(paletteColor.copy(alpha = 0.62f), size.minDimension * radius, Offset(size.width * x, size.height * y), style = Stroke(5.dp.toPx()))
}

private fun DrawScope.drawConnectedPoints(palette: ArtworkPalette) {
    val points = listOf(Offset(size.width * 0.24f, size.height * 0.7f), Offset(size.width * 0.48f, size.height * 0.58f), Offset(size.width * 0.72f, size.height * 0.72f))
    drawLine(palette.accent.copy(alpha = 0.62f), points[0], points[1], 3.dp.toPx(), StrokeCap.Round)
    drawLine(palette.accent.copy(alpha = 0.62f), points[1], points[2], 3.dp.toPx(), StrokeCap.Round)
    points.forEach { drawCircle(palette.accent, 5.dp.toPx(), it) }
}

private fun DrawScope.drawBalancedBlocks(palette: ArtworkPalette, leftHeight: Float, rightHeight: Float) {
    drawLine(palette.ink.copy(alpha = 0.42f), Offset(size.width * 0.18f, size.height * 0.68f), Offset(size.width * 0.82f, size.height * 0.68f), 4.dp.toPx(), StrokeCap.Round)
    drawRoundRect(palette.container, Offset(size.width * 0.24f, size.height * (0.68f - leftHeight)), Size(size.width * 0.18f, size.height * leftHeight), CornerRadius(12.dp.toPx()))
    drawRoundRect(palette.accent.copy(alpha = 0.48f), Offset(size.width * 0.58f, size.height * (0.68f - rightHeight)), Size(size.width * 0.18f, size.height * rightHeight), CornerRadius(12.dp.toPx()))
}

private fun DrawScope.drawLinkedForms(palette: ArtworkPalette, separated: Boolean) {
    val gap = if (separated) 0.34f else 0.2f
    drawCircle(palette.container, size.minDimension * 0.14f, Offset(size.width * (0.5f - gap / 2), size.height * 0.46f))
    drawCircle(palette.accent.copy(alpha = 0.38f), size.minDimension * 0.14f, Offset(size.width * (0.5f + gap / 2), size.height * 0.46f))
    drawPath(Path().apply {
        moveTo(size.width * 0.26f, size.height * 0.72f)
        quadraticTo(size.width * 0.5f, size.height * 0.58f, size.width * 0.74f, size.height * 0.72f)
    }, palette.accent, style = Stroke(4.dp.toPx(), cap = StrokeCap.Round))
}

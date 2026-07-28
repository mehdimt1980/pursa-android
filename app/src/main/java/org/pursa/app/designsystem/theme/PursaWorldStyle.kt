package org.pursa.app.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

enum class PursaWorldMotif {
    Truth,
    Justice,
    Friendship,
    Reflection,
}

data class PursaWorldStyle(
    val motif: PursaWorldMotif,
    val accent: Color,
    val onAccent: Color,
    val container: Color,
    val onContainer: Color,
    val soft: Color,
)

@Composable
@ReadOnlyComposable
fun pursaWorldStyle(worldId: String): PursaWorldStyle {
    val colors = PursaTheme.semanticColors
    return when (worldId) {
        "truth" -> PursaWorldStyle(
            motif = PursaWorldMotif.Truth,
            accent = colors.truth,
            onAccent = colors.onTruth,
            container = colors.truthContainer,
            onContainer = colors.onTruthContainer,
            soft = colors.truthSoft,
        )
        "justice" -> PursaWorldStyle(
            motif = PursaWorldMotif.Justice,
            accent = colors.justice,
            onAccent = colors.onJustice,
            container = colors.justiceContainer,
            onContainer = colors.onJusticeContainer,
            soft = colors.justiceSoft,
        )
        "friendship" -> PursaWorldStyle(
            motif = PursaWorldMotif.Friendship,
            accent = colors.friendship,
            onAccent = colors.onFriendship,
            container = colors.friendshipContainer,
            onContainer = colors.onFriendshipContainer,
            soft = colors.friendshipSoft,
        )
        else -> PursaWorldStyle(
            motif = PursaWorldMotif.Reflection,
            accent = colors.brand,
            onAccent = colors.onBrand,
            container = colors.brandContainer,
            onContainer = colors.onBrandContainer,
            soft = colors.canvasSecondary,
        )
    }
}

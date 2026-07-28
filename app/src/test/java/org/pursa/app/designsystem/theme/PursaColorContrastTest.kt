package org.pursa.app.designsystem.theme

import androidx.compose.ui.graphics.Color
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

class PursaColorContrastTest {
    private val colors = PursaLightSemanticColors

    @Test
    fun criticalTextPairsMeetNormalTextContrast() {
        listOf(
            "primary button" to (colors.brand to colors.onBrand),
            "brand container" to (colors.brandContainer to colors.onBrandContainer),
            "reading surface" to (colors.readingSurface to colors.inkStrong),
            "muted text" to (colors.readingSurface to colors.inkMuted),
            "truth container" to (colors.truthContainer to colors.onTruthContainer),
            "justice container" to (colors.justiceContainer to colors.onJusticeContainer),
            "friendship container" to (colors.friendshipContainer to colors.onFriendshipContainer),
            "reflection container" to (colors.reflectionContainer to colors.onReflectionContainer),
            "success message" to (colors.successContainer to colors.onSuccessContainer),
            "warning message" to (colors.warningContainer to colors.onWarningContainer),
            "info message" to (colors.infoContainer to colors.onInfoContainer),
        ).forEach { (name, pair) ->
            assertContrastAtLeast(name, pair.first, pair.second, MinimumNormalTextContrast)
        }
    }

    @Test
    fun worldAccentBoundariesMeetNonTextContrastOnSoftBackgrounds() {
        listOf(
            "truth accent" to (colors.truthSoft to colors.truth),
            "justice accent" to (colors.justiceSoft to colors.justice),
            "friendship accent" to (colors.friendshipSoft to colors.friendship),
            "brand accent" to (colors.canvasSecondary to colors.brand),
        ).forEach { (name, pair) ->
            assertContrastAtLeast(name, pair.first, pair.second, MinimumNonTextContrast)
        }
    }

    private fun assertContrastAtLeast(
        name: String,
        background: Color,
        foreground: Color,
        minimum: Double,
    ) {
        val ratio = contrastRatio(background, foreground)
        assertTrue("$name contrast was $ratio", ratio >= minimum)
    }

    private fun contrastRatio(first: Color, second: Color): Double {
        val firstLuminance = first.relativeLuminance()
        val secondLuminance = second.relativeLuminance()
        val lighter = max(firstLuminance, secondLuminance)
        val darker = min(firstLuminance, secondLuminance)
        return (lighter + 0.05) / (darker + 0.05)
    }

    private fun Color.relativeLuminance(): Double =
        0.2126 * red.toLinear() + 0.7152 * green.toLinear() + 0.0722 * blue.toLinear()

    private fun Float.toLinear(): Double {
        val value = toDouble()
        return if (value <= 0.03928) {
            value / 12.92
        } else {
            ((value + 0.055) / 1.055).pow(2.4)
        }
    }

    private companion object {
        const val MinimumNormalTextContrast = 4.5
        const val MinimumNonTextContrast = 3.0
    }
}

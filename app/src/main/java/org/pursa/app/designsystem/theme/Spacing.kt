package org.pursa.app.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class PursaSpacing(
    val none: Dp = 0.dp,
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 16.dp,
    val large: Dp = 24.dp,
    val extraLarge: Dp = 32.dp,
    val huge: Dp = 48.dp,
)

@Immutable
data class PursaSizes(
    val minimumTouchTarget: Dp = 48.dp,
    val compactScreenPadding: Dp = 20.dp,
    val screenPadding: Dp = 28.dp,
    val contentMaxWidth: Dp = 560.dp,
    val iconSmall: Dp = 18.dp,
    val iconMedium: Dp = 24.dp,
    val welcomeMark: Dp = 72.dp,
    val topBarHeight: Dp = 56.dp,
)

val LocalPursaSpacing = staticCompositionLocalOf { PursaSpacing() }
val LocalPursaSizes = staticCompositionLocalOf { PursaSizes() }

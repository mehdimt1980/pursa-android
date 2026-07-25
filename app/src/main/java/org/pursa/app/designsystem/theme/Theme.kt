package org.pursa.app.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

val LocalPursaSemanticColors = staticCompositionLocalOf { PursaLightSemanticColors }

object PursaTheme {
    val spacing: PursaSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalPursaSpacing.current

    val sizes: PursaSizes
        @Composable
        @ReadOnlyComposable
        get() = LocalPursaSizes.current

    val semanticColors: PursaSemanticColors
        @Composable
        @ReadOnlyComposable
        get() = LocalPursaSemanticColors.current
}

@Composable
fun PursaTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalPursaSpacing provides PursaSpacing(),
        LocalPursaSizes provides PursaSizes(),
        LocalPursaSemanticColors provides PursaLightSemanticColors,
    ) {
        MaterialTheme(
            colorScheme = PursaColorScheme,
            typography = PursaTypography,
            shapes = PursaShapes,
            content = content,
        )
    }
}

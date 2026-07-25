package org.pursa.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val PursaLightColorScheme = lightColorScheme(
    primary = PursaPrimary,
    onPrimary = PursaOnPrimary,
    primaryContainer = PursaPrimaryContainer,
    onPrimaryContainer = PursaOnPrimaryContainer,
    secondary = PursaSecondary,
    onSecondary = PursaOnSecondary,
    tertiary = PursaTertiary,
    onTertiary = PursaOnTertiary,
    background = PursaBackground,
    onBackground = PursaOnBackground,
    surface = PursaSurface,
    onSurface = PursaOnSurface,
    surfaceVariant = PursaSurfaceVariant,
    onSurfaceVariant = PursaOnSurfaceVariant,
)

@Composable
fun PursaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = PursaLightColorScheme,
        typography = PursaTypography,
        shapes = PursaShapes,
        content = content,
    )
}

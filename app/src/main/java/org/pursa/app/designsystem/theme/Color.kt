package org.pursa.app.designsystem.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val PursaColorScheme = lightColorScheme(
    primary = Color(0xFF5E466A),
    onPrimary = Color(0xFFFDF7FF),
    primaryContainer = Color(0xFFEBD8F2),
    onPrimaryContainer = Color(0xFF23102D),
    secondary = Color(0xFF7A5A25),
    onSecondary = Color(0xFFFFF8ED),
    secondaryContainer = Color(0xFFF4DEB7),
    onSecondaryContainer = Color(0xFF281800),
    tertiary = Color(0xFF316B67),
    onTertiary = Color(0xFFF3FFFC),
    tertiaryContainer = Color(0xFFC7E9E4),
    onTertiaryContainer = Color(0xFF00201D),
    background = Color(0xFFFFFBF4),
    onBackground = Color(0xFF24211B),
    surface = Color(0xFFFFFCF7),
    onSurface = Color(0xFF24211B),
    surfaceVariant = Color(0xFFF0E7DC),
    onSurfaceVariant = Color(0xFF4F473D),
    outline = Color(0xFF7E756A),
    outlineVariant = Color(0xFFD4C7B8),
    error = Color(0xFF8C3B32),
    onError = Color(0xFFFFFBFF),
    errorContainer = Color(0xFFFFDAD5),
    onErrorContainer = Color(0xFF3A0906),
)

data class PursaSemanticColors(
    val curiosity: Color,
    val onCuriosity: Color,
    val curiosityContainer: Color,
    val onCuriosityContainer: Color,
    val reflection: Color,
    val onReflection: Color,
    val discovery: Color,
    val onDiscovery: Color,
    val success: Color,
    val onSuccess: Color,
    val successContainer: Color,
    val onSuccessContainer: Color,
    val warning: Color,
    val onWarning: Color,
    val warningContainer: Color,
    val onWarningContainer: Color,
)

val PursaLightSemanticColors = PursaSemanticColors(
    curiosity = Color(0xFF8A5B17),
    onCuriosity = Color(0xFFFFFBF4),
    curiosityContainer = Color(0xFFF7DFB8),
    onCuriosityContainer = Color(0xFF2D1900),
    reflection = Color(0xFF6A5368),
    onReflection = Color(0xFFFFF7FC),
    discovery = Color(0xFF316B67),
    onDiscovery = Color(0xFFF3FFFC),
    success = Color(0xFF3F6B45),
    onSuccess = Color(0xFFF7FFF6),
    successContainer = Color(0xFFCDEBCF),
    onSuccessContainer = Color(0xFF08210C),
    warning = Color(0xFF83551F),
    onWarning = Color(0xFFFFFBF4),
    warningContainer = Color(0xFFF6D9A9),
    onWarningContainer = Color(0xFF2B1700),
)

package org.pursa.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import org.pursa.app.ui.welcome.WelcomeScreen

@Composable
fun PursaRoot() {
    PursaRtlRoot {
        WelcomeScreen(onPrimaryAction = {})
    }
}

@Composable
fun PursaRtlRoot(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        content()
    }
}

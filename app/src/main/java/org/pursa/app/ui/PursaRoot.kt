package org.pursa.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import org.pursa.app.PursaApp
import org.pursa.app.navigation.PursaNavGraph

@Composable
fun PursaRoot() {
    val container = (LocalContext.current.applicationContext as PursaApp).container
    PursaRtlRoot {
        PursaNavGraph(
            storyRepository = container.storyContentRepository,
            progressRepository = container.missionProgressRepository,
        )
    }
}

@Composable
fun PursaRtlRoot(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        content()
    }
}

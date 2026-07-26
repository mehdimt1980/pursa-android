package org.pursa.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.LayoutDirection
import org.pursa.app.content.data.AssetStoryDataSource
import org.pursa.app.content.data.LocalStoryContentRepository
import org.pursa.app.navigation.PursaNavGraph

@Composable
fun PursaRoot() {
    val context = LocalContext.current
    val storyRepository = remember(context.applicationContext) {
        LocalStoryContentRepository(
            dataSource = AssetStoryDataSource(context.applicationContext.assets),
        )
    }
    PursaRtlRoot {
        PursaNavGraph(storyRepository = storyRepository)
    }
}

@Composable
fun PursaRtlRoot(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        content()
    }
}

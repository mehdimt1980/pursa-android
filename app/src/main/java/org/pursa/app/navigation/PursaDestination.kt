package org.pursa.app.navigation

object PursaRouteArgs {
    const val WorldId = "worldId"
    const val StoryId = "storyId"
}

sealed class PursaDestination(val route: String) {
    data object Welcome : PursaDestination("welcome")

    data object Home : PursaDestination("home")

    data object WorldDetail : PursaDestination("world/{${PursaRouteArgs.WorldId}}") {
        fun createRoute(worldId: String): String = "world/$worldId"
    }

    data object Story : PursaDestination("story/{${PursaRouteArgs.StoryId}}") {
        fun createRoute(storyId: String): String = "story/$storyId"
    }
}

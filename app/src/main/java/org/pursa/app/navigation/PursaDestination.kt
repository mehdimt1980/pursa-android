package org.pursa.app.navigation

object PursaRouteArgs {
    const val WorldId = "worldId"
    const val StoryId = "storyId"
    const val JournalStoryId = "journalStoryId"
}

sealed class PursaDestination(val route: String) {
    data object Welcome : PursaDestination("welcome")

    data object Home : PursaDestination("home")

    data object Settings : PursaDestination("settings")

    data object JournalList : PursaDestination("journal")

    data object JournalDetail : PursaDestination("journal/{${PursaRouteArgs.JournalStoryId}}") {
        fun createRoute(storyId: String): String = "journal/$storyId"
    }

    data object WorldDetail : PursaDestination("world/{${PursaRouteArgs.WorldId}}") {
        fun createRoute(worldId: String): String = "world/$worldId"
    }

    data object Story : PursaDestination("story/{${PursaRouteArgs.StoryId}}") {
        fun createRoute(storyId: String): String = "story/$storyId"
    }
}

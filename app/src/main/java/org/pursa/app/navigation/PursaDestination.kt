package org.pursa.app.navigation

object PursaRouteArgs {
    const val WorldId = "worldId"
}

sealed class PursaDestination(val route: String) {
    data object Welcome : PursaDestination("welcome")

    data object Home : PursaDestination("home")

    data object WorldDetail : PursaDestination("world/{${PursaRouteArgs.WorldId}}") {
        fun createRoute(worldId: String): String = "world/$worldId"
    }
}

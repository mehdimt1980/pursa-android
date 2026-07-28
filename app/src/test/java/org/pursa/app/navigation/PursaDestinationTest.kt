package org.pursa.app.navigation

import org.junit.Assert.assertEquals
import org.junit.Test
import org.pursa.app.feature.home.PursaWorlds

class PursaDestinationTest {
    @Test
    fun routesAreCentralizedAndStable() {
        assertEquals("welcome", PursaDestination.Welcome.route)
        assertEquals("home", PursaDestination.Home.route)
        assertEquals("settings", PursaDestination.Settings.route)
        assertEquals("world/{worldId}", PursaDestination.WorldDetail.route)
        assertEquals("story/{storyId}", PursaDestination.Story.route)
    }

    @Test
    fun worldDetailRouteUsesWorldIdPathSegment() {
        assertEquals(
            "world/truth",
            PursaDestination.WorldDetail.createRoute(PursaWorlds.TruthId),
        )
    }

    @Test
    fun storyRouteUsesStoryIdPathSegment() {
        assertEquals(
            "story/truth_broken_vase",
            PursaDestination.Story.createRoute("truth_broken_vase"),
        )
    }
}

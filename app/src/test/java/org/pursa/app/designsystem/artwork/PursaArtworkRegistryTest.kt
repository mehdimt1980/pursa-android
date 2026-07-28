package org.pursa.app.designsystem.artwork

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PursaArtworkRegistryTest {
    @Test
    fun artworkKeysAreStableUniqueLowerSnakeCaseValues() {
        val keys = PursaArtworkRegistry.all.map { it.key }

        assertEquals(keys.size, keys.toSet().size)
        assertTrue(keys.all { it.matches(Regex("^[a-z][a-z0-9_]*$")) })
    }

    @Test
    fun registryContainsExpectedOfflineArtworkInventory() {
        assertEquals(3, PursaArtworkRegistry.worldArtwork.size)
        assertEquals(12, PursaArtworkRegistry.storyArtwork.size)
        assertEquals(2, PursaArtworkRegistry.stateArtwork.size)
        assertEquals(18, PursaArtworkRegistry.all.size)
    }

    @Test
    fun storyArtworkUsesStoryPlacementAndDecorativeAccessibility() {
        PursaArtworkRegistry.storyArtwork.forEach { descriptor ->
            assertEquals(PursaArtworkPlacement.Story, descriptor.placement)
            assertEquals(PursaArtworkAccessibility.Decorative, descriptor.accessibility)
            assertTrue(descriptor.key.startsWith("story_"))
            assertTrue(descriptor.worldId in setOf("truth", "justice", "friendship"))
        }
    }

    @Test
    fun unknownKeysResolveToLocalFallbackWithoutReflection() {
        val lookup = PursaArtworkRegistry.require("story_missing")

        assertTrue(lookup is PursaArtworkLookup.Missing)
        assertEquals("state_content_unavailable", PursaArtworkRegistry.descriptorFor("story_missing").key)
    }

    @Test
    fun worldKeysResolveForAllProductionWorlds() {
        listOf("truth", "justice", "friendship").forEach { worldId ->
            val descriptor = PursaArtworkRegistry.descriptorFor(PursaArtworkRegistry.worldKey(worldId))

            assertEquals("world_$worldId", descriptor.key)
            assertEquals(worldId, descriptor.worldId)
            assertEquals(PursaArtworkPlacement.World, descriptor.placement)
        }
    }
}

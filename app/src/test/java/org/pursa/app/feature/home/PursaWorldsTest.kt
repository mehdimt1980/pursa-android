package org.pursa.app.feature.home

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PursaWorldsTest {
    @Test
    fun worldListUsesStablePhaseFourIdsInOrder() {
        assertEquals(
            listOf(
                PursaWorlds.TruthId,
                PursaWorlds.JusticeId,
                PursaWorlds.FriendshipId,
            ),
            PursaWorlds.all.map { it.id },
        )
    }

    @Test
    fun eachWorldHasFourStarterQuestions() {
        assertTrue(PursaWorlds.all.all { it.sampleQuestionResIds.size == 4 })
    }

    @Test
    fun findByIdReturnsExpectedWorld() {
        assertEquals(PursaWorlds.TruthId, PursaWorlds.findById(PursaWorlds.TruthId)?.id)
        assertEquals(PursaWorlds.JusticeId, PursaWorlds.findById(PursaWorlds.JusticeId)?.id)
        assertEquals(PursaWorlds.FriendshipId, PursaWorlds.findById(PursaWorlds.FriendshipId)?.id)
    }

    @Test
    fun findByIdReturnsNullForUnknownWorld() {
        assertNull(PursaWorlds.findById("unknown"))
        assertNull(PursaWorlds.findById(null))
    }

    @Test
    fun worldDisplayDataIsComplete() {
        PursaWorlds.all.forEach { world ->
            assertNotNull(world.accent)
            assertTrue(world.titleResId != 0)
            assertTrue(world.summaryResId != 0)
            assertTrue(world.detailResId != 0)
        }
    }
}

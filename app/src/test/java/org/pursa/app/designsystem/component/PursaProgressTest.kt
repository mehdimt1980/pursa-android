package org.pursa.app.designsystem.component

import org.junit.Assert.assertEquals
import org.junit.Test

class PursaProgressTest {
    @Test
    fun coerceProgressKeepsValuesInRange() {
        assertEquals(0f, coerceProgress(-0.2f), 0f)
        assertEquals(0.5f, coerceProgress(0.5f), 0f)
        assertEquals(1f, coerceProgress(1.8f), 0f)
    }

    @Test
    fun progressFromStepsHandlesInvalidAndOutOfRangeValues() {
        assertEquals(0f, progressFromSteps(1, 0), 0f)
        assertEquals(0f, progressFromSteps(-1, 4), 0f)
        assertEquals(0.5f, progressFromSteps(2, 4), 0f)
        assertEquals(1f, progressFromSteps(8, 4), 0f)
    }
}

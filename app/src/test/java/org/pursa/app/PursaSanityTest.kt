package org.pursa.app

import org.junit.Assert.assertTrue
import org.junit.Test
import org.pursa.app.core.ui.PursaTestTags

class PursaSanityTest {
    @Test
    fun welcomeTestTagsUseStableProjectNamespace() {
        assertTrue(PursaTestTags.WelcomeScreenRoot.startsWith("pursa:"))
        assertTrue(PursaTestTags.WelcomePrimaryAction.startsWith("pursa:"))
        assertTrue(PursaTestTags.HomeScreenRoot.startsWith("pursa:"))
        assertTrue(PursaTestTags.WorldDetailRoot.startsWith("pursa:"))
    }
}

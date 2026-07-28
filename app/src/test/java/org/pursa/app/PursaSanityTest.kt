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
        assertTrue(PursaTestTags.StoryScreenRoot.startsWith("pursa:"))
        assertTrue(PursaTestTags.WorldArtwork.startsWith("pursa:"))
        assertTrue(PursaTestTags.storyArtwork("story").startsWith("pursa:"))
        assertTrue(PursaTestTags.missionArtwork("mission").startsWith("pursa:"))
        assertTrue(PursaTestTags.journalArtwork("story").startsWith("pursa:"))
        assertTrue(PursaTestTags.storyOption("step", "option").startsWith("pursa:"))
    }
}

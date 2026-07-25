package org.pursa.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Rule
import org.junit.Test
import org.pursa.app.core.ui.PursaTestTags

class WelcomeScreenTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun welcomeScreenShowsInitialPersianContent() {
        composeRule
            .onNodeWithTag(PursaTestTags.WelcomeScreenRoot)
            .assertIsDisplayed()

        composeRule
            .onNodeWithTag(PursaTestTags.WelcomeAppName)
            .assertIsDisplayed()

        composeRule
            .onNodeWithTag(PursaTestTags.WelcomePrimaryAction)
            .assertIsDisplayed()
    }
}

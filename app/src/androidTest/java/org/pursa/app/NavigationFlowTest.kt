package org.pursa.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import org.junit.Rule
import org.junit.Test
import org.pursa.app.content.data.AssetStoryDataSource
import org.pursa.app.content.data.LocalStoryContentRepository
import org.pursa.app.core.ui.PursaTestTags
import org.pursa.app.designsystem.theme.PursaTheme
import org.pursa.app.navigation.PursaDestination
import org.pursa.app.navigation.PursaNavGraph
import org.pursa.app.ui.PursaRtlRoot

class NavigationFlowTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun welcomeIsInitialDestination() {
        composeRule
            .onNodeWithTag(PursaTestTags.WelcomeScreenRoot)
            .assertIsDisplayed()
    }

    @Test
    fun welcomeContinueShowsHomeAndRemovesWelcomeFromBackStack() {
        composeRule
            .onNodeWithTag(PursaTestTags.WelcomePrimaryAction)
            .performClick()

        composeRule
            .onNodeWithTag(PursaTestTags.HomeScreenRoot)
            .assertIsDisplayed()

        composeRule.activityRule.scenario.onActivity {
            it.onBackPressedDispatcher.onBackPressed()
        }

        composeRule.waitForIdle()
        composeRule
            .onAllNodesWithTag(PursaTestTags.WelcomeScreenRoot)
            .assertCountEquals(0)
    }

    @Test
    fun homeShowsThreeWorldCards() {
        composeRule
            .onNodeWithTag(PursaTestTags.WelcomePrimaryAction)
            .performClick()

        composeRule
            .onNodeWithTag(PursaTestTags.HomeWorldTruth)
            .assertIsDisplayed()
        composeRule
            .onNodeWithTag(PursaTestTags.HomeWorldJustice)
            .assertIsDisplayed()
        composeRule
            .onNodeWithTag(PursaTestTags.HomeWorldFriendship)
            .assertIsDisplayed()
        composeRule
            .onNodeWithTag(PursaTestTags.HomeJournalAction)
            .assertIsDisplayed()
    }

    @Test
    fun truthWorldOpensDetailAndBackReturnsHome() {
        composeRule
            .onNodeWithTag(PursaTestTags.WelcomePrimaryAction)
            .performClick()

        composeRule
            .onNodeWithTag(PursaTestTags.HomeWorldTruth)
            .performClick()

        composeRule
            .onNodeWithTag(PursaTestTags.WorldDetailRoot)
            .assertIsDisplayed()
        composeRule
            .onNodeWithTag(PursaTestTags.WorldDetailQuestions)
            .assertIsDisplayed()

        composeRule
            .onNodeWithTag(PursaTestTags.WorldDetailBack)
            .performClick()

        composeRule
            .onNodeWithTag(PursaTestTags.HomeScreenRoot)
            .assertIsDisplayed()
    }

    @Test
    fun invalidWorldRouteShowsSafeFallback() {
        composeRule.setContent {
            InvalidWorldRouteContent()
        }

        composeRule
            .onNodeWithTag(PursaTestTags.WorldDetailRoot)
            .assertIsDisplayed()
    }
}

@Composable
private fun InvalidWorldRouteContent() {
    val startDestination = remember {
        PursaDestination.WorldDetail.createRoute("missing")
    }

    PursaTheme {
        PursaRtlRoot {
            val context = LocalContext.current
            val repository = remember(context.applicationContext) {
                LocalStoryContentRepository(
                    dataSource = AssetStoryDataSource(context.applicationContext.assets),
                )
            }
            val navController = rememberNavController()
            LaunchedEffect(Unit) {
                navController.navigate(startDestination)
            }
            PursaNavGraph(
                storyRepository = repository,
                progressRepository = FakeMissionProgressRepository(),
                journalRepository = FakeReflectionJournalRepository(),
                navController = navController,
                startDestination = PursaDestination.Home.route,
            )
        }
    }
}

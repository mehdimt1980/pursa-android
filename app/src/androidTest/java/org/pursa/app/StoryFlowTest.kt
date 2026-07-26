package org.pursa.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertDoesNotExist
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
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

class StoryFlowTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun truthWorldDisplaysRealMissionCard() {
        openTruthWorld()

        composeRule
            .onNodeWithTag(PursaTestTags.MissionTruthBrokenVase)
            .assertIsDisplayed()
    }

    @Test
    fun justiceAndFriendshipDoNotDisplayFakeMissionCards() {
        openWorld(PursaTestTags.HomeWorldJustice)
        composeRule
            .onNodeWithTag(PursaTestTags.MissionTruthBrokenVase)
            .assertDoesNotExist()

        composeRule
            .onNodeWithTag(PursaTestTags.WorldDetailBack)
            .performClick()
        openWorldFromHome(PursaTestTags.HomeWorldFriendship)
        composeRule
            .onNodeWithTag(PursaTestTags.MissionTruthBrokenVase)
            .assertDoesNotExist()
    }

    @Test
    fun missionOpensStoryAndRequiresSelectionBeforeContinuingChoiceStep() {
        openStory()

        composeRule
            .onNodeWithTag(PursaTestTags.StoryScreenRoot)
            .assertIsDisplayed()

        composeRule
            .onNodeWithTag(PursaTestTags.StoryContinue)
            .performScrollTo()
            .performClick()

        composeRule
            .onNodeWithTag(PursaTestTags.StoryContinue)
            .performScrollTo()
            .assertIsNotEnabled()

        composeRule
            .onNodeWithTag(PursaTestTags.storyOption("first_choice", "tell_truth"))
            .performScrollTo()
            .performClick()
            .assertIsSelected()
    }

    @Test
    fun previousNavigationPreservesSelection() {
        openStory()
        composeRule.onNodeWithTag(PursaTestTags.StoryContinue).performScrollTo().performClick()
        composeRule.onNodeWithTag(PursaTestTags.storyOption("first_choice", "tell_truth")).performScrollTo().performClick()
        composeRule.onNodeWithTag(PursaTestTags.StoryContinue).performScrollTo().performClick()
        composeRule.onNodeWithTag(PursaTestTags.StoryPrevious).performScrollTo().performClick()

        composeRule
            .onNodeWithTag(PursaTestTags.storyOption("first_choice", "tell_truth"))
            .performScrollTo()
            .assertIsSelected()
    }

    @Test
    fun completingMissionShowsSummaryAndReturnsToTruthWorld() {
        openStory()
        continueNarrative()
        chooseAndContinue("first_choice", "tell_truth")
        chooseAndContinue("reason_focus", "truth_telling")
        chooseAndContinue("mother_view", "trust_matters")
        chooseAndContinue("changed_condition", "partly_changes")
        chooseAndContinue("final_reflection", "partly_changed")

        composeRule
            .onNodeWithTag(PursaTestTags.StorySummaryRoot)
            .assertIsDisplayed()

        composeRule
            .onNodeWithTag(PursaTestTags.StoryReturnToWorld)
            .performScrollTo()
            .performClick()

        composeRule
            .onNodeWithTag(PursaTestTags.WorldDetailRoot)
            .assertIsDisplayed()
    }

    @Test
    fun invalidStoryIdShowsSafeError() {
        composeRule.setContent {
            InvalidStoryRouteContent()
        }

        composeRule
            .onNodeWithTag(PursaTestTags.StoryErrorMessage)
            .assertIsDisplayed()
    }

    private fun openStory() {
        openTruthWorld()
        composeRule
            .onNodeWithTag(PursaTestTags.MissionTruthBrokenVase)
            .performScrollTo()
            .performClick()
    }

    private fun openTruthWorld() {
        openWorld(PursaTestTags.HomeWorldTruth)
    }

    private fun openWorld(worldTag: String) {
        composeRule
            .onNodeWithTag(PursaTestTags.WelcomePrimaryAction)
            .performClick()
        openWorldFromHome(worldTag)
    }

    private fun openWorldFromHome(worldTag: String) {
        composeRule
            .onNodeWithTag(worldTag)
            .performClick()
    }

    private fun continueNarrative() {
        composeRule
            .onNodeWithTag(PursaTestTags.StoryContinue)
            .performScrollTo()
            .performClick()
    }

    private fun chooseAndContinue(
        stepId: String,
        optionId: String,
    ) {
        composeRule
            .onNodeWithTag(PursaTestTags.storyOption(stepId, optionId))
            .performScrollTo()
            .performClick()
        composeRule
            .onNodeWithTag(PursaTestTags.StoryContinue)
            .performScrollTo()
            .performClick()
    }
}

@Composable
private fun InvalidStoryRouteContent() {
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
                navController.navigate(PursaDestination.Story.createRoute("missing_story"))
            }
            PursaNavGraph(
                storyRepository = repository,
                navController = navController,
                startDestination = PursaDestination.Home.route,
            )
        }
    }
}

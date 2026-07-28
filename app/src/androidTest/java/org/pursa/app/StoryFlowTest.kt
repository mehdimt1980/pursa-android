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
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
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
    fun truthWorldDisplaysFourRealMissionCards() {
        openTruthWorld()

        composeRule
            .onNodeWithTag(PursaTestTags.MissionTruthBrokenVase)
            .assertIsDisplayed()
        composeRule
            .onNodeWithTag(PursaTestTags.mission("truth_group_photo"))
            .assertIsDisplayed()
        composeRule
            .onNodeWithTag(PursaTestTags.mission("truth_strange_news"))
            .assertIsDisplayed()
        composeRule
            .onNodeWithTag(PursaTestTags.mission("truth_friend_secret"))
            .assertIsDisplayed()
    }

    @Test
    fun truthWorldDisplaysNewMissionTitles() {
        openTruthWorld()

        composeRule.onAllNodesWithText("عکس گروهی").onFirst().assertIsDisplayed()
        composeRule.onAllNodesWithText("خبر عجیب").onFirst().assertIsDisplayed()
        composeRule.onAllNodesWithText("راز دوست").onFirst().assertIsDisplayed()
    }

    @Test
    fun justiceWorldDisplaysFourRealMissionCards() {
        openWorld(PursaTestTags.HomeWorldJustice)

        composeRule
            .onNodeWithTag(PursaTestTags.mission("justice_last_cake"))
            .assertIsDisplayed()
        composeRule
            .onNodeWithTag(PursaTestTags.mission("justice_class_representative"))
            .assertIsDisplayed()
        composeRule
            .onNodeWithTag(PursaTestTags.mission("justice_playground_rule"))
            .assertIsDisplayed()
        composeRule
            .onNodeWithTag(PursaTestTags.mission("justice_team_prize"))
            .assertIsDisplayed()
    }

    @Test
    fun justiceWorldDisplaysMissionTitles() {
        openWorld(PursaTestTags.HomeWorldJustice)

        composeRule.onAllNodesWithText("آخرین تکه‌ی کیک").onFirst().assertIsDisplayed()
        composeRule.onAllNodesWithText("نماینده‌ی کلاس").onFirst().assertIsDisplayed()
        composeRule.onAllNodesWithText("قانون تازه‌ی حیاط").onFirst().assertIsDisplayed()
        composeRule.onAllNodesWithText("جایزه‌ی کار گروهی").onFirst().assertIsDisplayed()
    }

    @Test
    fun friendshipDoesNotDisplayFakeMissionCards() {
        openWorld(PursaTestTags.HomeWorldFriendship)

        composeRule
            .onNodeWithTag(PursaTestTags.MissionTruthBrokenVase)
            .assertDoesNotExist()
        composeRule
            .onNodeWithTag(PursaTestTags.mission("justice_last_cake"))
            .assertDoesNotExist()
        composeRule
            .onNodeWithTag(PursaTestTags.mission("justice_team_prize"))
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
    fun groupPhotoMissionOpensRequiresSelectionAndCompletes() {
        openMission("truth_group_photo")
        composeRule.onAllNodesWithText("عکس گروهی").onFirst().assertIsDisplayed()
        continueNarrative()
        assertCurrentStepRequiresSelection()

        chooseAndContinue("share_choice", "find_agreement")
        chooseAndContinue("reason_focus", "people_consent")
        chooseAndContinue("memory_view", "not_whole_truth")
        chooseAndContinue("caption_changes_meaning", "partly_mislead")
        chooseAndContinue("nika_consent_view", "person_has_say")
        chooseAndContinue("final_reflection", "more_careful")
        assertSummaryReturnsToTruthWorld()
    }

    @Test
    fun strangeNewsMissionOpensRequiresSelectionAndCompletes() {
        openMission("truth_strange_news")
        composeRule.onAllNodesWithText("خبر عجیب").onFirst().assertIsDisplayed()
        continueNarrative()
        assertCurrentStepRequiresSelection()

        chooseAndContinue("forward_choice", "check_source")
        chooseAndContinue("trust_reason", "trusted_source")
        chooseAndContinue("friend_repetition_view", "not_enough")
        chooseAndContinue("same_original_mistake", "repetition_some_clue")
        chooseAndContinue("evidence_compare", "find_origin")
        chooseAndContinue("urgency_view", "checking_matters")
        chooseAndContinue("final_reflection", "clearer_difference")
        assertSummaryReturnsToTruthWorld()
    }

    @Test
    fun friendSecretMissionOpensRequiresSelectionAndCompletes() {
        openMission("truth_friend_secret")
        composeRule.onAllNodesWithText("راز دوست").onFirst().assertIsDisplayed()
        continueNarrative()
        assertCurrentStepRequiresSelection()

        chooseAndContinue("secret_choice", "talk_first")
        chooseAndContinue("reason_focus", "chance_to_fix")
        chooseAndContinue("loyalty_view", "loyalty_can_question")
        chooseAndContinue("unfair_harm", "partly_changes")
        chooseAndContinue("private_talk_view", "fair_chance")
        chooseAndContinue("final_reflection", "method_matters")
        assertSummaryReturnsToTruthWorld()
    }

    @Test
    fun lastCakeMissionOpensRequiresSelectionAndCompletes() {
        openMissionFromWorld(PursaTestTags.HomeWorldJustice, "justice_last_cake")
        composeRule.onAllNodesWithText("آخرین تکه‌ی کیک").onFirst().assertIsDisplayed()
        continueNarrative()
        assertCurrentStepRequiresSelection()

        chooseAndContinue("cake_choice", "sara_none")
        chooseAndContinue("reason_focus", "need_matters")
        chooseAndContinue("sara_view", "need_changes")
        chooseAndContinue("arad_view", "effort_limited")
        chooseAndContinue("lottery_question", "only_impartial")
        chooseAndContinue("unequal_pieces", "depends_reason")
        chooseAndContinue("final_reflection", "more_complicated")
        assertSummaryReturnsToWorld()
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
        openMission("truth_broken_vase")
    }

    private fun openMission(storyId: String) {
        openMissionFromWorld(PursaTestTags.HomeWorldTruth, storyId)
    }

    private fun openMissionFromWorld(
        worldTag: String,
        storyId: String,
    ) {
        openWorld(worldTag)
        composeRule
            .onNodeWithTag(missionTag(storyId))
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

    private fun assertCurrentStepRequiresSelection() {
        composeRule
            .onNodeWithTag(PursaTestTags.StoryContinue)
            .performScrollTo()
            .assertIsNotEnabled()
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

    private fun assertSummaryReturnsToTruthWorld() {
        assertSummaryReturnsToWorld()
    }

    private fun assertSummaryReturnsToWorld() {
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

    private fun missionTag(storyId: String): String = when (storyId) {
        "truth_broken_vase" -> PursaTestTags.MissionTruthBrokenVase
        else -> PursaTestTags.mission(storyId)
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
                progressRepository = FakeMissionProgressRepository(),
                navController = navController,
                startDestination = PursaDestination.Home.route,
            )
        }
    }
}

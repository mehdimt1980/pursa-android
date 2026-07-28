package org.pursa.app.core.ui

object PursaTestTags {
    const val WelcomeScreenRoot = "pursa:welcome:root"
    const val WelcomeAppName = "pursa:welcome:app-name"
    const val WelcomePrimaryAction = "pursa:welcome:primary-action"
    const val HomeScreenRoot = "pursa:home:root"
    const val HomeWorldTruth = "pursa:home:world:truth"
    const val HomeWorldJustice = "pursa:home:world:justice"
    const val HomeWorldFriendship = "pursa:home:world:friendship"
    const val WorldDetailRoot = "pursa:world:detail:root"
    const val WorldDetailBack = "pursa:world:detail:back"
    const val WorldDetailQuestions = "pursa:world:detail:questions"
    const val MissionListRoot = "pursa:mission-list:root"
    const val MissionTruthBrokenVase = "pursa:mission:truth-broken-vase"
    const val StoryScreenRoot = "pursa:story:root"
    const val StoryContinue = "pursa:story:continue"
    const val StoryPrevious = "pursa:story:previous"
    const val StoryProgress = "pursa:story:progress"
    const val StorySummaryRoot = "pursa:story:summary:root"
    const val StoryReturnToWorld = "pursa:story:return-to-world"
    const val StoryErrorMessage = "pursa:story:error"
    const val DesignSystemPrimaryButton = "pursa:design-system:primary-button"
    const val DesignSystemLoadingButton = "pursa:design-system:loading-button"
    const val DesignSystemSelectableChip = "pursa:design-system:selectable-chip"
    const val DesignSystemProgress = "pursa:design-system:progress"
    const val DesignSystemMessage = "pursa:design-system:message"

    fun mission(storyId: String): String = "pursa:mission:$storyId"

    fun storyOption(stepId: String, optionId: String): String = "pursa:story:option:$stepId:$optionId"
}

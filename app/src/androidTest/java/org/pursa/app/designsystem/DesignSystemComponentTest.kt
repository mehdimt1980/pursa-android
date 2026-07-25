package org.pursa.app.designsystem

import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.LayoutDirection
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.pursa.app.core.ui.PursaTestTags
import org.pursa.app.designsystem.component.PursaButton
import org.pursa.app.designsystem.component.PursaLinearProgress
import org.pursa.app.designsystem.component.PursaMessage
import org.pursa.app.designsystem.component.PursaSelectableChip
import org.pursa.app.designsystem.theme.PursaTheme
import org.pursa.app.ui.PursaRtlRoot

class DesignSystemComponentTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun primaryButtonIsDisplayedAndClickable() {
        composeRule.setContent {
            PursaTheme {
                PursaButton(
                    text = "ادامه",
                    onClick = {},
                    modifier = Modifier.testTag(PursaTestTags.DesignSystemPrimaryButton),
                )
            }
        }

        composeRule
            .onNodeWithTag(PursaTestTags.DesignSystemPrimaryButton)
            .assertIsDisplayed()
            .assertHasClickAction()
    }

    @Test
    fun loadingButtonIsDisabledForClicking() {
        composeRule.setContent {
            PursaTheme {
                PursaButton(
                    text = "در حال آماده‌سازی",
                    onClick = {},
                    loading = true,
                    modifier = Modifier.testTag(PursaTestTags.DesignSystemLoadingButton),
                )
            }
        }

        composeRule
            .onNodeWithTag(PursaTestTags.DesignSystemLoadingButton)
            .assertIsDisplayed()
            .assertIsNotEnabled()
    }

    @Test
    fun selectableChipExposesSelectedState() {
        composeRule.setContent {
            PursaTheme {
                PursaSelectableChip(
                    text = "انتخاب شده",
                    selected = true,
                    onSelectedChange = {},
                    modifier = Modifier.testTag(PursaTestTags.DesignSystemSelectableChip),
                )
            }
        }

        composeRule
            .onNodeWithTag(PursaTestTags.DesignSystemSelectableChip)
            .assertIsSelected()
    }

    @Test
    fun progressExposesProgressSemantics() {
        composeRule.setContent {
            PursaTheme {
                PursaLinearProgress(
                    progress = 0.5f,
                    modifier = Modifier.testTag(PursaTestTags.DesignSystemProgress),
                )
            }
        }

        composeRule
            .onNodeWithTag(PursaTestTags.DesignSystemProgress)
            .assert(
                SemanticsMatcher.expectValue(
                    SemanticsProperties.ProgressBarRangeInfo,
                    ProgressBarRangeInfo(0.5f, 0f..1f, 0),
                ),
            )
    }

    @Test
    fun messageIsDisplayed() {
        composeRule.setContent {
            PursaTheme {
                PursaMessage(
                    title = "پیام",
                    message = "متن کوتاه",
                    modifier = Modifier.testTag(PursaTestTags.DesignSystemMessage),
                )
            }
        }

        composeRule
            .onNodeWithTag(PursaTestTags.DesignSystemMessage)
            .assertIsDisplayed()
    }

    @Test
    fun appRootAppliesRtlLayoutDirection() {
        var observedDirection: LayoutDirection? = null

        composeRule.setContent {
            PursaTheme {
                PursaRtlRoot {
                    observedDirection = LocalLayoutDirection.current
                }
            }
        }

        assertEquals(LayoutDirection.Rtl, observedDirection)
    }
}

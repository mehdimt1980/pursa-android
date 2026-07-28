package org.pursa.app.feature.story

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import org.pursa.app.R
import org.pursa.app.content.model.PursaStoryOption
import org.pursa.app.content.model.PursaStoryStep
import org.pursa.app.core.ui.PursaTestTags
import org.pursa.app.designsystem.component.PursaCard
import org.pursa.app.designsystem.theme.PursaTheme

@Composable
fun StoryStepContent(
    step: PursaStoryStep,
    selectedOptionId: String?,
    onSelectOption: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (step) {
        is PursaStoryStep.Narrative -> NarrativeStepContent(step, modifier)
        is PursaStoryStep.SingleChoice -> OptionStepContent(
            title = step.question,
            options = step.options,
            stepId = step.id,
            selectedOptionId = selectedOptionId,
            onSelectOption = onSelectOption,
            modifier = modifier,
        )
        is PursaStoryStep.ReasonPrompt -> OptionStepContent(
            title = step.question,
            options = step.reasons,
            stepId = step.id,
            selectedOptionId = selectedOptionId,
            onSelectOption = onSelectOption,
            modifier = modifier,
        )
        is PursaStoryStep.Perspective -> PerspectiveStepContent(
            step = step,
            selectedOptionId = selectedOptionId,
            onSelectOption = onSelectOption,
            modifier = modifier,
        )
        is PursaStoryStep.Counterexample -> CounterexampleStepContent(
            step = step,
            selectedOptionId = selectedOptionId,
            onSelectOption = onSelectOption,
            modifier = modifier,
        )
        is PursaStoryStep.Reflection -> OptionStepContent(
            title = step.question,
            options = step.choices,
            stepId = step.id,
            selectedOptionId = selectedOptionId,
            onSelectOption = onSelectOption,
            modifier = modifier,
        )
    }
}

@Composable
private fun NarrativeStepContent(
    step: PursaStoryStep.Narrative,
    modifier: Modifier = Modifier,
) {
    PursaCard(
        title = step.title.orEmpty(),
        supportingText = step.body,
        containerColor = PursaTheme.semanticColors.readingSurface,
        borderColor = PursaTheme.semanticColors.outlineSoft,
        modifier = modifier.fillMaxWidth(),
    )
}

@Composable
private fun PerspectiveStepContent(
    step: PursaStoryStep.Perspective,
    selectedOptionId: String?,
    onSelectOption: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(PursaTheme.spacing.medium),
    ) {
        PursaCard(
            title = step.speakerLabel,
            supportingText = step.viewpoint,
            containerColor = PursaTheme.semanticColors.readingSurface,
            borderColor = PursaTheme.semanticColors.outlineSoft,
            modifier = Modifier.fillMaxWidth(),
        )
        OptionStepContent(
            title = step.question,
            options = step.responses,
            stepId = step.id,
            selectedOptionId = selectedOptionId,
            onSelectOption = onSelectOption,
        )
    }
}

@Composable
private fun CounterexampleStepContent(
    step: PursaStoryStep.Counterexample,
    selectedOptionId: String?,
    onSelectOption: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(PursaTheme.spacing.medium),
    ) {
        PursaCard(
            title = step.title.orEmpty(),
            supportingText = step.scenario,
            containerColor = PursaTheme.semanticColors.readingSurface,
            borderColor = PursaTheme.semanticColors.outlineSoft,
            modifier = Modifier.fillMaxWidth(),
        )
        OptionStepContent(
            title = step.question,
            options = step.choices,
            stepId = step.id,
            selectedOptionId = selectedOptionId,
            onSelectOption = onSelectOption,
        )
    }
}

@Composable
private fun OptionStepContent(
    title: String,
    options: List<PursaStoryOption>,
    stepId: String,
    selectedOptionId: String?,
    onSelectOption: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(PursaTheme.spacing.medium),
    ) {
        PursaCard(
            title = title,
            containerColor = PursaTheme.semanticColors.readingSurface,
            borderColor = PursaTheme.semanticColors.outlineSoft,
            modifier = Modifier.fillMaxWidth(),
        )
        options.forEach { option ->
            val isSelected = option.id == selectedOptionId
            PursaCard(
                title = option.label,
                supportingText = if (isSelected) stringResource(R.string.story_selected_option) else null,
                onClick = { onSelectOption(option.id) },
                accentColor = if (isSelected) MaterialTheme.colorScheme.primary else null,
                containerColor = if (isSelected) {
                    PursaTheme.semanticColors.brandContainer
                } else {
                    PursaTheme.semanticColors.readingSurface
                },
                contentColor = if (isSelected) {
                    PursaTheme.semanticColors.onBrandContainer
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                borderColor = if (isSelected) {
                    PursaTheme.semanticColors.brand
                } else {
                    PursaTheme.semanticColors.outlineSoft
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(PursaTestTags.storyOption(stepId, option.id))
                    .semantics {
                        selected = isSelected
                        role = Role.RadioButton
                    },
            )
        }
    }
}

package org.pursa.app.content.validation

import org.pursa.app.content.model.PursaStory
import org.pursa.app.content.model.PursaStoryOption
import org.pursa.app.content.model.PursaStoryStep
import org.pursa.app.feature.home.PursaWorlds

class StoryContentValidator {
    fun validate(story: PursaStory): StoryValidationResult {
        val errors = mutableListOf<StoryValidationError>()
        validateStory(story, errors)
        validateSteps(story, errors)
        return if (errors.isEmpty()) {
            StoryValidationResult.Valid
        } else {
            StoryValidationResult.Invalid(errors)
        }
    }

    private fun validateStory(
        story: PursaStory,
        errors: MutableList<StoryValidationError>,
    ) {
        if (story.schemaVersion != SupportedSchemaVersion) {
            errors += error(story.id, null, StoryValidationErrorCode.UnsupportedSchemaVersion, "Unsupported schema version.")
        }
        if (story.contentRevision <= 0) {
            errors += error(story.id, null, StoryValidationErrorCode.InvalidContentRevision, "Content revision must be positive.")
        }
        if (story.id.isBlank()) {
            errors += error(story.id, null, StoryValidationErrorCode.BlankStoryId, "Story ID must not be blank.")
        } else if (!story.id.isStableId()) {
            errors += error(story.id, null, StoryValidationErrorCode.InvalidStoryIdFormat, "Story ID must use lowercase ASCII.")
        }
        if (story.worldId !in validWorldIds) {
            errors += error(story.id, null, StoryValidationErrorCode.InvalidWorldId, "Story world ID is not supported.")
        }
        if (!story.artworkKey.matches(artworkKeyRegex)) {
            errors += error(story.id, null, StoryValidationErrorCode.InvalidArtworkKey, "Story artwork key is invalid.")
        }
        requireNonBlank(story.title, story.id, null, "title", errors)
        requireNonBlank(story.summary, story.id, null, "summary", errors)
        if (story.recommendedMinAge !in 6..18 || story.recommendedMaxAge < story.recommendedMinAge) {
            errors += error(story.id, null, StoryValidationErrorCode.InvalidAgeRange, "Story age range is invalid.")
        }
        if (story.estimatedDurationMinutes <= 0) {
            errors += error(story.id, null, StoryValidationErrorCode.InvalidDuration, "Estimated duration must be positive.")
        }
        if (story.themes.isEmpty() || story.themes.any { it.isBlank() }) {
            errors += error(story.id, null, StoryValidationErrorCode.EmptyThemes, "Story must include at least one theme.")
        }
        requireNonBlank(story.introduction.title, story.id, null, "introduction title", errors)
        requireNonBlank(story.introduction.body, story.id, null, "introduction body", errors)
        if (story.steps.isEmpty()) {
            errors += error(story.id, null, StoryValidationErrorCode.EmptySteps, "Story must include at least one step.")
        }
        requireNonBlank(story.completion.title, story.id, null, "completion title", errors, StoryValidationErrorCode.EmptyCompletion)
        requireNonBlank(story.completion.reflection, story.id, null, "completion reflection", errors, StoryValidationErrorCode.EmptyCompletion)
    }

    private fun validateSteps(
        story: PursaStory,
        errors: MutableList<StoryValidationError>,
    ) {
        val seenStepIds = mutableSetOf<String>()
        story.steps.forEach { step ->
            if (!seenStepIds.add(step.id)) {
                errors += error(story.id, step.id, StoryValidationErrorCode.DuplicateStepId, "Step ID must be unique.")
            }
            if (step.id.isBlank()) {
                errors += error(story.id, step.id, StoryValidationErrorCode.BlankText, "Step ID must not be blank.")
            } else if (!step.id.isStableId()) {
                errors += error(story.id, step.id, StoryValidationErrorCode.InvalidStepIdFormat, "Step ID must use lowercase ASCII.")
            }

            when (step) {
                is PursaStoryStep.Narrative -> {
                    step.title?.let { requireNonBlank(it, story.id, step.id, "narrative title", errors) }
                    requireNonBlank(step.body, story.id, step.id, "narrative body", errors)
                }
                is PursaStoryStep.SingleChoice -> {
                    requireNonBlank(step.question, story.id, step.id, "single choice question", errors)
                    validateOptions(story.id, step.id, step.options, 2..4, errors)
                }
                is PursaStoryStep.ReasonPrompt -> {
                    requireNonBlank(step.question, story.id, step.id, "reason prompt question", errors)
                    validateOptions(story.id, step.id, step.reasons, 2..5, errors)
                }
                is PursaStoryStep.Perspective -> {
                    requireNonBlank(step.speakerLabel, story.id, step.id, "speaker label", errors)
                    requireNonBlank(step.viewpoint, story.id, step.id, "viewpoint", errors)
                    requireNonBlank(step.question, story.id, step.id, "perspective question", errors)
                    validateOptions(story.id, step.id, step.responses, 2..2, errors)
                }
                is PursaStoryStep.Counterexample -> {
                    step.title?.let { requireNonBlank(it, story.id, step.id, "counterexample title", errors) }
                    requireNonBlank(step.scenario, story.id, step.id, "counterexample scenario", errors)
                    requireNonBlank(step.question, story.id, step.id, "counterexample question", errors)
                    validateOptions(story.id, step.id, step.choices, 3..3, errors)
                }
                is PursaStoryStep.Reflection -> {
                    requireNonBlank(step.question, story.id, step.id, "reflection question", errors)
                    validateOptions(story.id, step.id, step.choices, 2..4, errors)
                }
            }
        }
    }

    private fun validateOptions(
        storyId: String,
        stepId: String,
        options: List<PursaStoryOption>,
        countRange: IntRange,
        errors: MutableList<StoryValidationError>,
    ) {
        if (options.size !in countRange) {
            errors += error(storyId, stepId, StoryValidationErrorCode.InvalidOptionCount, "Option count is outside the allowed range.")
        }
        val seenOptionIds = mutableSetOf<String>()
        options.forEach { option ->
            if (!seenOptionIds.add(option.id)) {
                errors += error(storyId, stepId, StoryValidationErrorCode.DuplicateOptionId, "Option ID must be unique within the step.")
            }
            if (!option.id.isStableId()) {
                errors += error(storyId, stepId, StoryValidationErrorCode.InvalidOptionIdFormat, "Option ID must use lowercase ASCII.")
            }
            requireNonBlank(option.label, storyId, stepId, "option label", errors)
        }
    }

    private fun requireNonBlank(
        value: String,
        storyId: String?,
        stepId: String?,
        fieldName: String,
        errors: MutableList<StoryValidationError>,
        code: StoryValidationErrorCode = StoryValidationErrorCode.BlankText,
    ) {
        if (value.isBlank()) {
            errors += error(storyId, stepId, code, "$fieldName must not be blank.")
        }
    }

    private fun error(
        storyId: String?,
        stepId: String?,
        code: StoryValidationErrorCode,
        message: String,
    ): StoryValidationError = StoryValidationError(
        storyId = storyId?.takeIf { it.isNotBlank() },
        stepId = stepId?.takeIf { it.isNotBlank() },
        code = code,
        message = message,
    )

    private fun String.isStableId(): Boolean = stableIdRegex.matches(this)

    companion object {
        const val SupportedSchemaVersion = 1

        private val stableIdRegex = Regex("^[a-z][a-z0-9_]*$")
        private val artworkKeyRegex = Regex("^story_[a-z][a-z0-9_]*$")
        private val validWorldIds = setOf(
            PursaWorlds.TruthId,
            PursaWorlds.JusticeId,
            PursaWorlds.FriendshipId,
        )
    }
}

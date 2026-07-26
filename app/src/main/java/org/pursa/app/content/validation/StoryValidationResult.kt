package org.pursa.app.content.validation

data class StoryValidationError(
    val storyId: String?,
    val stepId: String?,
    val code: StoryValidationErrorCode,
    val message: String,
)

enum class StoryValidationErrorCode {
    UnsupportedSchemaVersion,
    BlankStoryId,
    InvalidStoryIdFormat,
    InvalidWorldId,
    BlankText,
    InvalidAgeRange,
    InvalidDuration,
    EmptyThemes,
    EmptySteps,
    DuplicateStepId,
    InvalidStepIdFormat,
    InvalidOptionCount,
    DuplicateOptionId,
    InvalidOptionIdFormat,
    EmptyCompletion,
    ParseFailure,
    InvalidManifest,
}

sealed interface StoryValidationResult {
    data object Valid : StoryValidationResult
    data class Invalid(val errors: List<StoryValidationError>) : StoryValidationResult
}

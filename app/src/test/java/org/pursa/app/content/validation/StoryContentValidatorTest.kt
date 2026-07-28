package org.pursa.app.content.validation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.pursa.app.content.data.JsonStoryParser
import org.pursa.app.content.data.StoryParseResult
import org.pursa.app.content.model.PursaStory
import org.pursa.app.content.model.PursaStoryOption
import org.pursa.app.content.model.PursaStoryStep
import org.pursa.app.content.sampleStoryJson

class StoryContentValidatorTest {
    private val parser = JsonStoryParser()
    private val validator = StoryContentValidator()

    @Test
    fun validSampleStoryPasses() {
        assertEquals(StoryValidationResult.Valid, validator.validate(sampleStory()))
    }

    @Test
    fun duplicateStepIdsFail() {
        val story = sampleStory().let { it.copy(steps = it.steps + it.steps.first()) }

        assertInvalid(story, StoryValidationErrorCode.DuplicateStepId)
    }

    @Test
    fun blankStoryIdFails() {
        assertInvalid(sampleStory().copy(id = ""), StoryValidationErrorCode.BlankStoryId)
    }

    @Test
    fun invalidStoryIdFormatFails() {
        assertInvalid(sampleStory().copy(id = "Truth Story"), StoryValidationErrorCode.InvalidStoryIdFormat)
    }

    @Test
    fun invalidWorldIdFails() {
        assertInvalid(sampleStory().copy(worldId = "space"), StoryValidationErrorCode.InvalidWorldId)
    }

    @Test
    fun invalidAgeRangeFails() {
        assertInvalid(sampleStory().copy(recommendedMinAge = 12, recommendedMaxAge = 8), StoryValidationErrorCode.InvalidAgeRange)
    }

    @Test
    fun emptyStepsFail() {
        assertInvalid(sampleStory().copy(steps = emptyList()), StoryValidationErrorCode.EmptySteps)
    }

    @Test
    fun singleChoiceWithTooFewOptionsFails() {
        val story = replaceFirstSingleChoiceOptions(listOf(PursaStoryOption("one", "One")))

        assertInvalid(story, StoryValidationErrorCode.InvalidOptionCount)
    }

    @Test
    fun singleChoiceWithTooManyOptionsFails() {
        val story = replaceFirstSingleChoiceOptions(
            listOf("one", "two", "three", "four", "five").map { PursaStoryOption(it, it) },
        )

        assertInvalid(story, StoryValidationErrorCode.InvalidOptionCount)
    }

    @Test
    fun duplicateOptionIdsFail() {
        val story = replaceFirstSingleChoiceOptions(
            listOf(
                PursaStoryOption("same", "One"),
                PursaStoryOption("same", "Two"),
            ),
        )

        assertInvalid(story, StoryValidationErrorCode.DuplicateOptionId)
    }

    @Test
    fun blankRequiredTextFails() {
        assertInvalid(sampleStory().copy(title = ""), StoryValidationErrorCode.BlankText)
    }

    @Test
    fun unsupportedSchemaVersionFails() {
        assertInvalid(sampleStory().copy(schemaVersion = 2), StoryValidationErrorCode.UnsupportedSchemaVersion)
    }

    @Test
    fun invalidContentRevisionFails() {
        assertInvalid(sampleStory().copy(contentRevision = 0), StoryValidationErrorCode.InvalidContentRevision)
    }

    private fun replaceFirstSingleChoiceOptions(options: List<PursaStoryOption>): PursaStory {
        val story = sampleStory()
        return story.copy(
            steps = story.steps.map { step ->
                if (step is PursaStoryStep.SingleChoice) step.copy(options = options) else step
            },
        )
    }

    private fun assertInvalid(
        story: PursaStory,
        code: StoryValidationErrorCode,
    ) {
        val result = validator.validate(story)
        assertTrue(result is StoryValidationResult.Invalid)
        assertTrue((result as StoryValidationResult.Invalid).errors.any { it.code == code })
    }

    private fun sampleStory(): PursaStory =
        (parser.parseStory(sampleStoryJson()) as StoryParseResult.Success).value
}

package org.pursa.app.content.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.pursa.app.content.sampleManifestJson
import org.pursa.app.content.sampleStoryJson
import org.pursa.app.content.model.PursaStoryStep

class JsonStoryParserTest {
    private val parser = JsonStoryParser()

    @Test
    fun validManifestParsesSuccessfully() {
        val result = parser.parseManifest(sampleManifestJson())

        assertTrue(result is StoryParseResult.Success)
        assertEquals("fa", (result as StoryParseResult.Success).value.locale)
    }

    @Test
    fun validSampleStoryParsesAllSupportedStepTypes() {
        val result = parser.parseStory(sampleStoryJson())

        assertTrue(result is StoryParseResult.Success)
        val story = (result as StoryParseResult.Success).value
        assertEquals("truth_broken_vase", story.id)
        assertTrue(story.steps.any { it is PursaStoryStep.Narrative })
        assertTrue(story.steps.any { it is PursaStoryStep.SingleChoice })
        assertTrue(story.steps.any { it is PursaStoryStep.ReasonPrompt })
        assertTrue(story.steps.any { it is PursaStoryStep.Perspective })
        assertTrue(story.steps.any { it is PursaStoryStep.Counterexample })
        assertTrue(story.steps.any { it is PursaStoryStep.Reflection })
    }

    @Test
    fun unknownStepTypeFailsClearly() {
        val json = sampleStoryJson().replace("\"type\": \"narrative\"", "\"type\": \"branch\"")

        val result = parser.parseStory(json)

        assertTrue(result is StoryParseResult.Failure)
    }

    @Test
    fun malformedJsonReturnsFailure() {
        val result = parser.parseStory("{")

        assertTrue(result is StoryParseResult.Failure)
    }

    @Test
    fun missingRequiredFieldFails() {
        val json = sampleStoryJson().replace("\"title\": \"گلدان شکسته\",", "")

        val result = parser.parseStory(json)

        assertTrue(result is StoryParseResult.Failure)
    }

    @Test
    fun persianContentIsPreserved() {
        val story = (parser.parseStory(sampleStoryJson()) as StoryParseResult.Success).value

        assertEquals("گلدان شکسته", story.title)
        assertTrue(story.introduction.body.contains("آرین"))
    }
}

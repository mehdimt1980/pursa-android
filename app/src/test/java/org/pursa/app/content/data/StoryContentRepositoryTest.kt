package org.pursa.app.content.data

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.pursa.app.content.FakeStoryDataSource
import org.pursa.app.content.sampleManifestJson
import org.pursa.app.content.sampleStoryJson

class StoryContentRepositoryTest {
    @Test
    fun manifestReturnsTruthStorySummary() = runBlocking {
        val result = repository().loadAllStorySummaries()

        assertTrue(result is StoryContentResult.Success)
        assertEquals("truth_broken_vase", (result as StoryContentResult.Success).value.single().id)
    }

    @Test
    fun filteringByTruthReturnsSampleMission() = runBlocking {
        val result = repository().loadStoriesByWorld("truth")

        assertTrue(result is StoryContentResult.Success)
        assertEquals(1, (result as StoryContentResult.Success).value.size)
    }

    @Test
    fun filteringByJusticeReturnsNoMissions() = runBlocking {
        val result = repository().loadStoriesByWorld("justice")

        assertTrue(result is StoryContentResult.Success)
        assertTrue((result as StoryContentResult.Success).value.isEmpty())
    }

    @Test
    fun validStoryIdLoadsSuccessfully() = runBlocking {
        val result = repository().loadStory("truth_broken_vase")

        assertTrue(result is StoryContentResult.Success)
        assertEquals("گلدان شکسته", (result as StoryContentResult.Success).value.title)
    }

    @Test
    fun unknownStoryIdReturnsNotFound() = runBlocking {
        assertEquals(StoryContentResult.NotFound, repository().loadStory("missing"))
    }

    @Test
    fun invalidStoryContentReturnsInvalidContent() = runBlocking {
        val result = repository(storyJson = sampleStoryJson().replace("\"id\": \"truth_broken_vase\"", "\"id\": \"bad id\""))
            .loadStory("truth_broken_vase")

        assertTrue(result is StoryContentResult.InvalidContent)
    }

    @Test
    fun repeatedLoadUsesSameBehavior() = runBlocking {
        val repository = repository()

        assertTrue(repository.loadStory("truth_broken_vase") is StoryContentResult.Success)
        assertTrue(repository.loadStory("truth_broken_vase") is StoryContentResult.Success)
    }

    private fun repository(
        storyJson: String = sampleStoryJson(),
    ): StoryContentRepository = LocalStoryContentRepository(
        dataSource = FakeStoryDataSource(
            mapOf(
                LocalStoryContentRepository.ManifestPath to sampleManifestJson(),
                "content/fa/stories/truth/truth_broken_vase.json" to storyJson,
            ),
        ),
    )
}

package org.pursa.app.content.data

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.pursa.app.content.FakeStoryDataSource
import org.pursa.app.content.productionFriendshipStoryIds
import org.pursa.app.content.productionJusticeStoryIds
import org.pursa.app.content.productionStoryIds
import org.pursa.app.content.productionStoryAssets
import org.pursa.app.content.productionTruthStoryIds
import org.pursa.app.content.sampleManifestJson
import org.pursa.app.content.sampleStoryJson

class StoryContentRepositoryTest {
    @Test
    fun manifestReturnsProductionStorySummariesInOrder() = runBlocking {
        val result = repository().loadAllStorySummaries()

        assertTrue(result is StoryContentResult.Success)
        assertEquals(productionStoryIds, (result as StoryContentResult.Success).value.map { it.id })
    }

    @Test
    fun filteringByTruthReturnsProductionMissions() = runBlocking {
        val result = repository().loadStoriesByWorld("truth")

        assertTrue(result is StoryContentResult.Success)
        assertEquals(productionTruthStoryIds, (result as StoryContentResult.Success).value.map { it.id })
    }

    @Test
    fun filteringByJusticeReturnsProductionMissions() = runBlocking {
        val result = repository().loadStoriesByWorld("justice")

        assertTrue(result is StoryContentResult.Success)
        assertEquals(productionJusticeStoryIds, (result as StoryContentResult.Success).value.map { it.id })
    }

    @Test
    fun filteringByFriendshipReturnsProductionMissions() = runBlocking {
        val result = repository().loadStoriesByWorld("friendship")

        assertTrue(result is StoryContentResult.Success)
        assertEquals(productionFriendshipStoryIds, (result as StoryContentResult.Success).value.map { it.id })
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
        val result = repository(
            assets = productionStoryAssets() +
                ("content/fa/stories/truth/truth_broken_vase.json" to
                    sampleStoryJson().replace("\"id\": \"truth_broken_vase\"", "\"id\": \"bad id\"")),
        )
            .loadStory("truth_broken_vase")

        assertTrue(result is StoryContentResult.InvalidContent)
    }

    @Test
    fun duplicateManifestStoryIdsReturnInvalidContent() = runBlocking {
        val manifest = sampleManifestJson().replace("\"id\": \"truth_group_photo\"", "\"id\": \"truth_broken_vase\"")

        val result = repository(manifestJson = manifest).loadAllStorySummaries()

        assertTrue(result is StoryContentResult.InvalidContent)
    }

    @Test
    fun duplicateManifestAssetPathsReturnInvalidContent() = runBlocking {
        val manifest = sampleManifestJson().replace(
            "content/fa/stories/truth/truth_group_photo.json",
            "content/fa/stories/truth/truth_broken_vase.json",
        )

        val result = repository(manifestJson = manifest).loadAllStorySummaries()

        assertTrue(result is StoryContentResult.InvalidContent)
    }

    @Test
    fun repeatedLoadUsesSameBehavior() = runBlocking {
        val repository = repository()

        assertTrue(repository.loadStory("truth_broken_vase") is StoryContentResult.Success)
        assertTrue(repository.loadStory("truth_broken_vase") is StoryContentResult.Success)
    }

    private fun repository(
        manifestJson: String = sampleManifestJson(),
        assets: Map<String, String> = productionStoryAssets(),
    ): StoryContentRepository = LocalStoryContentRepository(
        dataSource = FakeStoryDataSource(
            assets + (LocalStoryContentRepository.ManifestPath to manifestJson),
        ),
    )
}

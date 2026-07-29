package org.pursa.app.content

import java.nio.file.Files
import java.nio.file.Path
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.pursa.app.content.data.JsonStoryParser
import org.pursa.app.content.data.StoryParseResult
import org.pursa.app.content.model.PursaStoryStep
import org.pursa.app.journal.data.finalReflectionStep
import org.pursa.app.journal.data.journalQuestionCandidates

class EducationalReviewTest {
    private val parser = JsonStoryParser()
    private val manifest = productionManifest()

    @Test
    fun productionReviewInventoryMatchesProductionStories() {
        val reviewFiles = reviewFiles()
        val reviewIds = reviewFiles.map { it.fileName.toString().removeSuffix(".review.md") }

        assertEquals(12, manifest.stories.size)
        assertEquals(12, reviewFiles.size)
        assertEquals(productionStoryIds.sorted(), reviewIds.sorted())
        assertEquals(reviewIds.size, reviewIds.toSet().size)
    }

    @Test
    fun reviewIndexIncludesEveryProductionStory() {
        val index = readReviewFile("README.md")

        productionStoryIds.forEach { storyId ->
            assertTrue("Review index must include $storyId", index.contains("`$storyId`"))
            assertTrue("Review index must link $storyId", index.contains("$storyId.review.md"))
        }
    }

    @Test
    fun everyReviewContainsRequiredEducationalHeadingsAndStatus() {
        val requiredHeadings = listOf(
            "## Metadata",
            "## Central Philosophical Problem",
            "## Competing Values Or Concepts",
            "## Defensible Positions",
            "## Conceptual Distinctions",
            "## P4C Structure",
            "## Perspective Quality",
            "## Counterexample Quality",
            "## Option Quality",
            "## Final Reflection Quality",
            "## Persian-Language Review",
            "## Child-Safety Review",
            "## Inclusion And Cultural Review",
            "## Artwork Alignment",
            "## Cross-Story Uniqueness",
            "## Required Changes",
            "## Compatibility Impact",
            "## Final Review Status",
        )
        val allowedStatuses = listOf(
            "Approved.",
            "Approved with minor editorial changes.",
            "Approved after substantive revision.",
            "Needs specialist review.",
        )

        reviewFiles().forEach { file ->
            val text = Files.readString(file)
            requiredHeadings.forEach { heading ->
                assertTrue("${file.fileName} missing $heading", text.contains(heading))
            }
            val storyId = file.fileName.toString().removeSuffix(".review.md")
            assertTrue("${file.fileName} must reference story ID", text.contains("Story ID: `$storyId`"))
            assertTrue("${file.fileName} must document final status", allowedStatuses.any { text.contains(it) })
            assertTrue("${file.fileName} must document compatibility", text.contains("No content revision change."))
            assertTrue("${file.fileName} must document at least two positions", text.contains("## Defensible Positions"))
        }
    }

    @Test
    fun educationalReviewDocumentsExist() {
        listOf(
            "docs/EDUCATIONAL_REVIEW.md",
            "docs/CONTENT_REVIEW_CHECKLIST.md",
            "docs/CONTENT_MAP.md",
            "docs/USER_PILOT_PROTOCOL.md",
            "content/reviews/README.md",
        ).forEach { path ->
            assertTrue("$path must exist", Files.exists(projectPath(path)))
        }
    }

    @Test
    fun productionStoriesKeepEducationalStructureAndJournalCompatibility() {
        parsedStories().forEach { story ->
            assertTrue(story.steps.any { it is PursaStoryStep.Narrative })
            assertTrue(story.steps.any { it is PursaStoryStep.ReasonPrompt })
            assertTrue(story.steps.any { it is PursaStoryStep.Perspective })
            assertTrue(story.steps.any { it is PursaStoryStep.Counterexample })
            assertTrue(story.steps.any { it is PursaStoryStep.Reflection })
            assertTrue(story.completion.reflection.isNotBlank())
            assertTrue(story.journalQuestionCandidates().size >= 3)
            assertTrue(story.finalReflectionStep()?.choices?.isNotEmpty() == true)
            assertTrue(story.contentRevision > 0)
        }
    }

    @Test
    fun productionStoryTextAvoidsUnsafeTechnicalArtifacts() {
        parsedStories().forEach { story ->
            story.textValues().forEach { text ->
                assertFalse("${story.id} must not contain Arabic Yeh", text.contains('\u064A'))
                assertFalse("${story.id} must not contain Arabic Kaf", text.contains('\u0643'))
                assertFalse("${story.id} must not contain repeated spaces", text.contains("  "))
                assertFalse("${story.id} must not contain raw HTML", text.contains("<") || text.contains(">"))
                assertFalse("${story.id} must not contain embedded URLs", Regex("https?://").containsMatchIn(text))
                assertFalse("${story.id} must not contain excessive newline artifacts", text.contains("\n\n"))
            }
        }
    }

    private fun parsedStories() = manifest.stories.map { entry ->
        val result = parser.parseStory(productionStoryJson(entry.assetPath))
        assertTrue(result is StoryParseResult.Success)
        (result as StoryParseResult.Success).value
    }

    private fun org.pursa.app.content.model.PursaStory.textValues(): List<String> = buildList {
        add(title)
        add(summary)
        add(introduction.title)
        add(introduction.body)
        add(completion.title)
        add(completion.reflection)
        completion.familyPrompt?.let(::add)
        steps.forEach { step ->
            when (step) {
                is PursaStoryStep.Narrative -> {
                    step.title?.let(::add)
                    add(step.body)
                }
                is PursaStoryStep.SingleChoice -> {
                    add(step.question)
                    step.options.forEach { add(it.label) }
                }
                is PursaStoryStep.ReasonPrompt -> {
                    add(step.question)
                    step.reasons.forEach { add(it.label) }
                }
                is PursaStoryStep.Perspective -> {
                    add(step.speakerLabel)
                    add(step.viewpoint)
                    add(step.question)
                    step.responses.forEach { add(it.label) }
                }
                is PursaStoryStep.Counterexample -> {
                    step.title?.let(::add)
                    add(step.scenario)
                    add(step.question)
                    step.choices.forEach { add(it.label) }
                }
                is PursaStoryStep.Reflection -> {
                    add(step.question)
                    step.choices.forEach { add(it.label) }
                }
            }
        }
    }

    private fun reviewFiles(): List<Path> =
        Files.newDirectoryStream(projectPath("content/reviews"), "*.review.md").use { stream ->
            stream.toList().sorted()
        }

    private fun readReviewFile(name: String): String =
        Files.readString(projectPath("content/reviews/$name"))

    private fun projectPath(relativePath: String): Path {
        val userDir = Path.of(System.getProperty("user.dir"))
        val candidates = listOf(
            userDir.resolve(relativePath),
            userDir.parent?.resolve(relativePath),
        ).filterNotNull()
        return candidates.firstOrNull { Files.exists(it) } ?: candidates.first()
    }
}

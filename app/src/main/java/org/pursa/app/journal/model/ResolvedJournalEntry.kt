package org.pursa.app.journal.model

sealed interface ResolvedJournalEntry {
    val record: ReflectionJournalRecord

    data class Available(
        override val record: ReflectionJournalRecord,
        val storyTitle: String,
        val worldId: String,
        val revisitQuestion: String,
        val selectedReflectionLabel: String?,
        val contentChanged: Boolean,
    ) : ResolvedJournalEntry

    data class Incompatible(
        override val record: ReflectionJournalRecord,
        val storyTitle: String?,
        val worldId: String?,
    ) : ResolvedJournalEntry

    data class StoryUnavailable(
        override val record: ReflectionJournalRecord,
    ) : ResolvedJournalEntry
}

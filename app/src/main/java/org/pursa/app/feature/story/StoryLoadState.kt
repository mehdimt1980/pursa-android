package org.pursa.app.feature.story

import org.pursa.app.content.model.PursaStory
import org.pursa.app.content.state.StorySessionState

sealed interface StoryLoadState {
    data object Loading : StoryLoadState
    data class Success(
        val story: PursaStory,
        val sessionState: StorySessionState,
        val saveFailed: Boolean = false,
        val restoredFromSavedSession: Boolean = false,
        val journalEntryExists: Boolean = false,
        val selectedJournalQuestionStepId: String? = null,
        val journalSaveFailed: Boolean = false,
        val journalSaveSucceeded: Boolean = false,
    ) : StoryLoadState
    data object NotFound : StoryLoadState
    data object InvalidContent : StoryLoadState
    data object ReadFailure : StoryLoadState
}

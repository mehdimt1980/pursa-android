package org.pursa.app.feature.story

import org.pursa.app.content.model.PursaStory

sealed interface StoryLoadState {
    data object Loading : StoryLoadState
    data class Success(val story: PursaStory) : StoryLoadState
    data object NotFound : StoryLoadState
    data object InvalidContent : StoryLoadState
    data object ReadFailure : StoryLoadState
}

package org.pursa.app.content.data

import org.pursa.app.content.validation.StoryValidationError

sealed interface StoryContentResult<out T> {
    data class Success<T>(val value: T) : StoryContentResult<T>
    data object NotFound : StoryContentResult<Nothing>
    data class InvalidContent(val errors: List<StoryValidationError>) : StoryContentResult<Nothing>
    data class ReadFailure(val message: String) : StoryContentResult<Nothing>
}

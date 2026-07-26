package org.pursa.app.content.data

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.pursa.app.content.model.PursaStory

sealed interface StoryParseResult<out T> {
    data class Success<T>(val value: T) : StoryParseResult<T>
    data class Failure(
        val code: StoryParseErrorCode,
        val message: String,
    ) : StoryParseResult<Nothing>
}

enum class StoryParseErrorCode {
    MalformedJson,
    MissingRequiredField,
    UnknownStepType,
    InvalidStructure,
}

class JsonStoryParser(
    private val json: Json = defaultJson,
) {
    fun parseManifest(rawJson: String): StoryParseResult<ContentManifest> = parse {
        json.decodeFromString<ContentManifest>(rawJson)
    }

    fun parseStory(rawJson: String): StoryParseResult<PursaStory> = parse {
        json.decodeFromString<PursaStory>(rawJson)
    }

    private fun <T> parse(block: () -> T): StoryParseResult<T> = try {
        StoryParseResult.Success(block())
    } catch (exception: SerializationException) {
        StoryParseResult.Failure(
            code = exception.toParseErrorCode(),
            message = exception.message.orEmpty().take(160),
        )
    } catch (exception: IllegalArgumentException) {
        StoryParseResult.Failure(
            code = StoryParseErrorCode.InvalidStructure,
            message = exception.message.orEmpty().take(160),
        )
    }

    private fun SerializationException.toParseErrorCode(): StoryParseErrorCode {
        val text = message.orEmpty()
        return when {
            "Polymorphic serializer was not found" in text -> StoryParseErrorCode.UnknownStepType
            "Field" in text && "is required" in text -> StoryParseErrorCode.MissingRequiredField
            "Unexpected JSON token" in text -> StoryParseErrorCode.MalformedJson
            else -> StoryParseErrorCode.InvalidStructure
        }
    }

    companion object {
        val defaultJson = Json {
            classDiscriminator = "type"
            coerceInputValues = false
            ignoreUnknownKeys = false
            isLenient = false
        }
    }
}

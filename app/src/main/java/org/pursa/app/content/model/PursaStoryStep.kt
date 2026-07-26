package org.pursa.app.content.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface PursaStoryStep {
    val id: String

    @Serializable
    @SerialName("narrative")
    data class Narrative(
        override val id: String,
        val title: String? = null,
        val body: String,
    ) : PursaStoryStep

    @Serializable
    @SerialName("single_choice")
    data class SingleChoice(
        override val id: String,
        val question: String,
        val options: List<PursaStoryOption>,
    ) : PursaStoryStep

    @Serializable
    @SerialName("reason_prompt")
    data class ReasonPrompt(
        override val id: String,
        val question: String,
        val reasons: List<PursaStoryOption>,
    ) : PursaStoryStep

    @Serializable
    @SerialName("perspective")
    data class Perspective(
        override val id: String,
        val speakerLabel: String,
        val viewpoint: String,
        val question: String,
        val responses: List<PursaStoryOption>,
    ) : PursaStoryStep

    @Serializable
    @SerialName("counterexample")
    data class Counterexample(
        override val id: String,
        val title: String? = null,
        val scenario: String,
        val question: String,
        val choices: List<PursaStoryOption>,
    ) : PursaStoryStep

    @Serializable
    @SerialName("reflection")
    data class Reflection(
        override val id: String,
        val question: String,
        val choices: List<PursaStoryOption>,
    ) : PursaStoryStep
}

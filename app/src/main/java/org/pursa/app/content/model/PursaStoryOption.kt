package org.pursa.app.content.model

import kotlinx.serialization.Serializable

@Serializable
data class PursaStoryOption(
    val id: String,
    val label: String,
)

package org.pursa.app.content.data

import kotlinx.serialization.Serializable

@Serializable
data class ContentManifest(
    val schemaVersion: Int,
    val locale: String,
    val stories: List<ContentManifestStory>,
)

@Serializable
data class ContentManifestStory(
    val id: String,
    val worldId: String,
    val assetPath: String,
)

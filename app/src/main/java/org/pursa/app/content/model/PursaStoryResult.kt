package org.pursa.app.content.model

data class PursaStorySummary(
    val id: String,
    val worldId: String,
    val artworkKey: String,
    val title: String,
    val summary: String,
    val recommendedMinAge: Int,
    val recommendedMaxAge: Int,
    val estimatedDurationMinutes: Int,
    val themes: List<String>,
)

fun PursaStory.toSummary(): PursaStorySummary = PursaStorySummary(
    id = id,
    worldId = worldId,
    artworkKey = artworkKey,
    title = title,
    summary = summary,
    recommendedMinAge = recommendedMinAge,
    recommendedMaxAge = recommendedMaxAge,
    estimatedDurationMinutes = estimatedDurationMinutes,
    themes = themes,
)

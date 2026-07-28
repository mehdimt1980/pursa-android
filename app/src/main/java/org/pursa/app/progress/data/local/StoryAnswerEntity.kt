package org.pursa.app.progress.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "story_answers",
    primaryKeys = ["storyId", "stepId"],
    foreignKeys = [
        ForeignKey(
            entity = StorySessionEntity::class,
            parentColumns = ["storyId"],
            childColumns = ["storyId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index("storyId"),
    ],
)
data class StoryAnswerEntity(
    val storyId: String,
    val stepId: String,
    val selectedOptionId: String,
    val updatedAtEpochMillis: Long,
)

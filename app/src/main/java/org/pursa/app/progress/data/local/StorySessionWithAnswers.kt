package org.pursa.app.progress.data.local

import androidx.room.Embedded
import androidx.room.Relation

data class StorySessionWithAnswers(
    @Embedded val session: StorySessionEntity,
    @Relation(
        parentColumn = "storyId",
        entityColumn = "storyId",
    )
    val answers: List<StoryAnswerEntity>,
)

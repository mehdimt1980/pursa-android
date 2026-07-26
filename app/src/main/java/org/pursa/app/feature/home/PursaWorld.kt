package org.pursa.app.feature.home

import androidx.annotation.StringRes
import org.pursa.app.R

data class PursaWorld(
    val id: String,
    @param:StringRes val titleResId: Int,
    @param:StringRes val summaryResId: Int,
    @param:StringRes val detailResId: Int,
    val accent: PursaWorldAccent,
    val sampleQuestionResIds: List<Int>,
)

enum class PursaWorldAccent {
    Curiosity,
    Discovery,
    Reflection,
}

object PursaWorlds {
    const val TruthId = "truth"
    const val JusticeId = "justice"
    const val FriendshipId = "friendship"

    val all = listOf(
        PursaWorld(
            id = TruthId,
            titleResId = R.string.world_truth_title,
            summaryResId = R.string.world_truth_summary,
            detailResId = R.string.world_truth_detail,
            accent = PursaWorldAccent.Curiosity,
            sampleQuestionResIds = listOf(
                R.string.world_truth_question_1,
                R.string.world_truth_question_2,
                R.string.world_truth_question_3,
                R.string.world_truth_question_4,
            ),
        ),
        PursaWorld(
            id = JusticeId,
            titleResId = R.string.world_justice_title,
            summaryResId = R.string.world_justice_summary,
            detailResId = R.string.world_justice_detail,
            accent = PursaWorldAccent.Discovery,
            sampleQuestionResIds = listOf(
                R.string.world_justice_question_1,
                R.string.world_justice_question_2,
                R.string.world_justice_question_3,
                R.string.world_justice_question_4,
            ),
        ),
        PursaWorld(
            id = FriendshipId,
            titleResId = R.string.world_friendship_title,
            summaryResId = R.string.world_friendship_summary,
            detailResId = R.string.world_friendship_detail,
            accent = PursaWorldAccent.Reflection,
            sampleQuestionResIds = listOf(
                R.string.world_friendship_question_1,
                R.string.world_friendship_question_2,
                R.string.world_friendship_question_3,
                R.string.world_friendship_question_4,
            ),
        ),
    )

    fun findById(id: String?): PursaWorld? = all.firstOrNull { it.id == id }
}

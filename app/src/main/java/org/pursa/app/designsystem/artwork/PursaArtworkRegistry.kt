package org.pursa.app.designsystem.artwork

enum class PursaArtworkPlacement {
    World,
    Story,
    State,
}

enum class PursaArtworkAccessibility {
    Decorative,
    Informative,
}

enum class PursaArtworkScene {
    TruthWorld,
    JusticeWorld,
    FriendshipWorld,
    BrokenVase,
    GroupPhoto,
    StrangeNews,
    FriendSecret,
    LastCake,
    ClassRepresentative,
    PlaygroundRule,
    TeamPrize,
    NewFriend,
    DifficultPromise,
    WhoseSide,
    GameWithoutThem,
    StoryComplete,
    JournalEmpty,
    Fallback,
}

data class PursaArtworkDescriptor(
    val key: String,
    val worldId: String?,
    val placement: PursaArtworkPlacement,
    val scene: PursaArtworkScene,
    val accessibility: PursaArtworkAccessibility = PursaArtworkAccessibility.Decorative,
    val aspectRatio: Float = 1.45f,
)

sealed interface PursaArtworkLookup {
    data class Found(val descriptor: PursaArtworkDescriptor) : PursaArtworkLookup
    data class Missing(val requestedKey: String, val fallback: PursaArtworkDescriptor) : PursaArtworkLookup
}

object PursaArtworkRegistry {
    val worldArtwork: List<PursaArtworkDescriptor> = listOf(
        descriptor("world_truth", "truth", PursaArtworkPlacement.World, PursaArtworkScene.TruthWorld),
        descriptor("world_justice", "justice", PursaArtworkPlacement.World, PursaArtworkScene.JusticeWorld),
        descriptor("world_friendship", "friendship", PursaArtworkPlacement.World, PursaArtworkScene.FriendshipWorld),
    )

    val storyArtwork: List<PursaArtworkDescriptor> = listOf(
        descriptor("story_truth_broken_vase", "truth", PursaArtworkPlacement.Story, PursaArtworkScene.BrokenVase),
        descriptor("story_truth_group_photo", "truth", PursaArtworkPlacement.Story, PursaArtworkScene.GroupPhoto),
        descriptor("story_truth_strange_news", "truth", PursaArtworkPlacement.Story, PursaArtworkScene.StrangeNews),
        descriptor("story_truth_friend_secret", "truth", PursaArtworkPlacement.Story, PursaArtworkScene.FriendSecret),
        descriptor("story_justice_last_cake", "justice", PursaArtworkPlacement.Story, PursaArtworkScene.LastCake),
        descriptor("story_justice_class_representative", "justice", PursaArtworkPlacement.Story, PursaArtworkScene.ClassRepresentative),
        descriptor("story_justice_playground_rule", "justice", PursaArtworkPlacement.Story, PursaArtworkScene.PlaygroundRule),
        descriptor("story_justice_team_prize", "justice", PursaArtworkPlacement.Story, PursaArtworkScene.TeamPrize),
        descriptor("story_friendship_new_friend", "friendship", PursaArtworkPlacement.Story, PursaArtworkScene.NewFriend),
        descriptor("story_friendship_difficult_promise", "friendship", PursaArtworkPlacement.Story, PursaArtworkScene.DifficultPromise),
        descriptor("story_friendship_whose_side", "friendship", PursaArtworkPlacement.Story, PursaArtworkScene.WhoseSide),
        descriptor("story_friendship_game_without_them", "friendship", PursaArtworkPlacement.Story, PursaArtworkScene.GameWithoutThem),
    )

    val stateArtwork: List<PursaArtworkDescriptor> = listOf(
        descriptor("state_story_complete", null, PursaArtworkPlacement.State, PursaArtworkScene.StoryComplete),
        descriptor("state_journal_empty", null, PursaArtworkPlacement.State, PursaArtworkScene.JournalEmpty),
    )

    val fallback: PursaArtworkDescriptor =
        descriptor("state_content_unavailable", null, PursaArtworkPlacement.State, PursaArtworkScene.Fallback)

    val all: List<PursaArtworkDescriptor> = worldArtwork + storyArtwork + stateArtwork + fallback

    fun require(key: String): PursaArtworkLookup =
        all.firstOrNull { it.key == key }?.let(PursaArtworkLookup::Found)
            ?: PursaArtworkLookup.Missing(key, fallback)

    fun descriptorFor(key: String): PursaArtworkDescriptor = when (val lookup = require(key)) {
        is PursaArtworkLookup.Found -> lookup.descriptor
        is PursaArtworkLookup.Missing -> lookup.fallback
    }

    fun worldKey(worldId: String): String = "world_$worldId"

    private fun descriptor(
        key: String,
        worldId: String?,
        placement: PursaArtworkPlacement,
        scene: PursaArtworkScene,
    ): PursaArtworkDescriptor = PursaArtworkDescriptor(
        key = key,
        worldId = worldId,
        placement = placement,
        scene = scene,
    )
}

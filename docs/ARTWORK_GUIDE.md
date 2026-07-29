# Offline Artwork Guide

Pursa artwork is local, original, and deterministic. Phase 12 uses Compose-drawn vector geometry instead of bundled bitmap or remote image files.

## Architecture

Runtime artwork lives in:

```text
app/src/main/java/org/pursa/app/designsystem/artwork/
```

The central registry is `PursaArtworkRegistry`. It maps stable string keys to `PursaArtworkDescriptor` values. Screens render descriptors with `PursaArtwork`.

Story JSON stores only a stable `artworkKey`, for example:

```json
"artworkKey": "story_truth_broken_vase"
```

Content files must not store Android resource IDs, package names, URLs, file paths to remote media, or generated resource references. UI code resolves keys through the registry. Do not use `Resources.getIdentifier`, reflection, story-ID-specific drawable lookup, image loading libraries, or network-backed media.

## Key Naming

- World artwork: `world_truth`, `world_justice`, `world_friendship`
- Story artwork: `story_<story_id>`
- State artwork: `state_story_complete`, `state_journal_empty`, `state_content_unavailable`

Keys must be stable lowercase ASCII with underscores.

## Accessibility

Most artwork is decorative. Decorative artwork is hidden from accessibility semantics so TalkBack users hear the meaningful screen text and controls instead of abstract visual descriptions.

Use informative artwork only when the visual communicates information that is not otherwise present in text. In that case, provide a localized content description from Android string resources.

## Originality and Licensing

Phase 12 artwork is authored as code with simple geometric motifs, semantic design-system colors, and no copied media. It does not include external image files, stock art, generated image files, copyrighted characters, photos, logos, or cultural symbols that require separate permission.

Before adding non-code artwork in the future:

- document source, author, license, and modification rights;
- keep source files in a reviewed artwork source directory;
- export optimized runtime assets under Android resources;
- verify no personal child data appears in the asset;
- confirm the visual is culturally familiar without stereotyping or propaganda.

## New Artwork Workflow

1. Add or update the story JSON `artworkKey`.
2. Add one descriptor to `PursaArtworkRegistry`.
3. Add one scene renderer in `PursaArtwork`.
4. Keep colors semantic through `PursaTheme` or world styles.
5. Add or update registry and production-content tests.
6. Run lint, unit tests, debug assembly, release assembly, and instrumentation APK assembly.
7. Confirm generated APKs and build outputs are not staged.

## Review Checklist

- Artwork works fully offline.
- No runtime network, storage, media, or photo permission is introduced.
- No image-loading dependency is introduced.
- No remote URL is required to render the UI.
- Artwork remains calm, non-gamified, and age-appropriate for approximately ages 8-12.
- World identity does not rely on color alone.
- Large fonts and RTL layout do not cause overlap.
- Decorative artwork remains hidden from accessibility semantics.
- Production story tests prove every `artworkKey` resolves.

## Educational Alignment Review

Artwork review happens together with story review. Check:

- the image matches the story without adding new facts;
- the image does not imply blame, guilt, shame, fear, or the correct answer;
- character emotion remains neutral enough for inquiry;
- the image does not contradict the text;
- no embedded text, score, badge, or moral cue appears;
- the composition remains readable at mission-card size;
- the story review status is recorded in `content/reviews/`.

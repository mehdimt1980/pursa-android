# New Production Story Checklist

A production story is not complete until content, registry artwork, validation, and tests all agree.

## Required Fields

- `id`: stable lowercase ASCII story ID.
- `worldId`: one of `truth`, `justice`, or `friendship`.
- `artworkKey`: stable lowercase ASCII key in the form `story_<story_id>`.
- `contentRevision`: positive integer.
- age range, duration, themes, introduction, steps, and completion.

Do not bump `contentRevision` for artwork-only changes unless authored story text or story interaction semantics change.

## Content

- Register the story in the local manifest.
- Keep the manifest order as the pedagogical display order.
- Include narrative, choice, reasons, perspective-taking, counterexample, and reflection.
- Avoid correct answers, scores, points, rewards, badges, profiles, ranking, and personal disclosure.
- Keep Persian text natural, fictional, low-risk, and age-appropriate.

## Artwork

- Add a matching descriptor to `PursaArtworkRegistry`.
- Add or reuse a `PursaArtworkScene`.
- Use semantic colors and simple original geometry.
- Keep artwork decorative unless it carries unique information.
- Do not add external files or URLs without license review.

## Verification

Run:

```bash
./gradlew :app:lintDebug :app:testDebugUnitTest :app:assembleDebug --stacktrace
./gradlew :app:assembleRelease --stacktrace
./gradlew :app:assembleDebugAndroidTest --stacktrace
```

Run `./gradlew connectedDebugAndroidTest --stacktrace` when an emulator or device is available.

Also confirm:

- production story parsing and validation tests pass;
- `PursaArtworkRegistryTest` passes;
- no Room schema changes occur unless explicitly intended;
- no generated APK or build output is staged.

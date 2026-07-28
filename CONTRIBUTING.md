# Contributing to پرسا | Pursa

Thank you for helping build `پرسا | Pursa`. This project is early-stage, child-centered, Persian-first, privacy-preserving, and open source.

## Proposing Code Changes

1. Open an issue or discuss the change before large work.
2. Keep pull requests small and focused.
3. Follow the planned architecture in [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md).
4. Add tests for new logic.
5. Update documentation when behavior, architecture, privacy, or content rules change.

## Visual and Accessibility Checks

Before opening a UI change, run:

```bash
./gradlew :app:lintDebug :app:testDebugUnitTest :app:assembleDebug --stacktrace
./gradlew :app:assembleRelease --stacktrace
./gradlew :app:assembleDebugAndroidTest --stacktrace
```

Use [docs/QUALITY.md](docs/QUALITY.md) for manual visual, large-font, RTL, landscape, TalkBack, and Accessibility Scanner review.

Feature screens should use `PursaTheme.semanticColors`, Material semantic roles, and `pursaWorldStyle(worldId)` for world identity. Do not add raw hexadecimal colors in feature code. Decorative geometry must be non-interactive and hidden from accessibility.

## Proposing Educational Content

Content proposals should follow [docs/CONTENT_GUIDE.md](docs/CONTENT_GUIDE.md). A proposal should include:

- Target age range.
- Philosophical theme.
- Story premise.
- Central open question.
- Alternative viewpoints.
- Examples or counterexamples.
- Cultural and safety considerations.

Content must encourage inquiry rather than moral preaching or memorization.

## Branch Naming

Examples:

- `feature/story-engine`
- `feature/rtl-foundations`
- `docs/content-guide-update`
- `fix/navigation-state`

## Commit Guidance

- Use clear, specific commit messages.
- Keep unrelated changes in separate commits.
- Do not commit generated local files, signing keys, APKs, AABs, or personal data.

## Pull Request Expectations

Pull requests should explain:

- What changed.
- Why it changed.
- How it was tested.
- Any privacy, accessibility, RTL, or content implications.

## Testing Expectations

For Android code changes, contributors should run:

```bash
./gradlew :app:lintDebug :app:testDebugUnitTest :app:assembleDebug
```

Continuous integration verifies these same checks for pushes and pull requests involving `main`. Instrumentation tests should be run when an emulator or device is available, but they are not required by the Phase 2 CI workflow.

Do not claim tests passed unless they were actually run.

## Accessibility Checks

Check for:

- Readable text size.
- Sufficient contrast.
- Large touch targets.
- Meaningful labels for controls.
- Avoidance of interactions that depend only on color, sound, or motion.

## RTL Checks

Check that:

- Persian text is natural and correctly aligned.
- Layouts mirror correctly in RTL.
- Icons and navigation directions make sense in RTL.
- Mixed Persian and Latin text renders clearly.

## Content Review Requirements

Story and activity content must be reviewed for:

- Open-ended inquiry.
- Age suitability for approximately ages 8-12.
- Cultural familiarity without stereotypes.
- No political or ideological propaganda.
- No requests for sensitive personal disclosure.
- No single predetermined correct answer.

## New Production Story Requirements

Before a new production story is merged, follow [docs/NEW_STORY_CHECKLIST.md](docs/NEW_STORY_CHECKLIST.md). Each story must declare a stable `artworkKey`, and that key must resolve through `PursaArtworkRegistry`.

Artwork contributions must follow [docs/ARTWORK_GUIDE.md](docs/ARTWORK_GUIDE.md). Do not add external images, remote URLs, image-loading dependencies, or Android resource IDs inside story JSON without explicit review.

## Assets and Personal Data

- Do not include copyrighted media without permission.
- Do not include children's personal data.
- Do not include real names, phone numbers, emails, photos, voice recordings, or private family details.

## Collaboration

Be respectful, patient, and constructive. This is an educational project for children, and contributor behavior should reflect that responsibility.

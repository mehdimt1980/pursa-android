# AGENTS.md

This file is authoritative for Codex and other coding agents working in `pursa-android`.

## Project Context

`پرسا | Pursa` is a free and open-source Android app for philosophy education for Iranian children, approximately ages 8-12. The app is Persian-first, RTL-first, offline-first, privacy-preserving, and based on open-ended philosophical inquiry.

The Android application ID is `org.pursa.app`.

No production app exists yet. Do not describe planned features as implemented.

## Architectural Direction

The planned stack is Kotlin, Jetpack Compose, Material 3, Gradle Kotlin DSL, MVVM, StateFlow, Navigation Compose, Room, DataStore, Hilt, JUnit, Compose UI tests, and GitHub Actions.

The MVP should begin as a single Android app module. Premature multi-module architecture is not allowed.

Use feature-oriented packages with clear UI, domain, and data responsibilities. Preserve offline-first behavior and local-only storage unless a task explicitly changes that direction.

## Future Verification Commands

When the Android project exists, expected verification may include:

```bash
./gradlew :app:lintDebug :app:testDebugUnitTest :app:assembleDebug
./gradlew connectedDebugAndroidTest
```

Only report these commands as successful if they were actually run and completed successfully.

## Scope Discipline

- Inspect existing files before editing.
- Keep changes scoped to the requested task.
- Do not perform unrelated refactors.
- Do not silently add dependencies.
- Do not create network access unless the task explicitly authorizes it.
- Do not collect personal information.
- Do not introduce Firebase, analytics, ads, payments, accounts, push notifications, social features, or online AI in the MVP.

## Implementation Standards

- Preserve RTL and Persian support in UI, content, naming, and layout decisions.
- Do not add raw hexadecimal colors outside design-system theme files; feature screens must use semantic Pursa or Material color roles.
- World identity must not depend on color alone; combine title, text, semantic color, and motif or structural cues.
- Every interactive control must preserve at least a 48dp touch target.
- Critical text/background combinations must meet accessible contrast targets and be backed by tests where practical.
- New screens should include compact and large-font-friendly previews when practical.
- Decorative geometry must be hidden from accessibility semantics and must not intercept touch.
- Long-form reading content must stay width-constrained and readable at large font scales.
- Do not add external visual assets without explicit license review and documentation.
- Do not claim accessibility certification without evidence; report manual and automated checks accurately.
- Release builds must compile before a visual-quality phase is called complete, unless the local environment limitation is reported honestly.
- Avoid hard-coded user-facing strings; plan for localization resources.
- Use semantic names that describe product meaning, not temporary implementation details.
- Add tests for new logic.
- Keep content data separate from UI logic.
- Do not include copyrighted media unless permission and license details are documented.
- Never include real children's personal data.
- Authored story text belongs in reviewed assets, not Kotlin or Android string resources.
- Reusable runtime UI labels belong in Android string resources.
- Story IDs, step IDs, and option IDs must be stable lowercase ASCII.
- New stories must be registered in the local manifest, pass runtime validation, and include tests.
- Production story IDs and asset paths must be unique.
- Manifest story IDs and world IDs must match parsed story content.
- Each production mission needs genuine philosophical tension, reasons, perspective-taking, a counterexample, and reflection.
- Do not add serious-harm secret scenarios without a dedicated safety review.
- New production stories require parsing and validation tests.
- New production stories require a stable `artworkKey` in `story_<story_id>` form.
- Story artwork keys must resolve through the centralized `PursaArtworkRegistry`.
- Do not put Android resource IDs, drawable names, remote URLs, or image-loader configuration in story content.
- Do not use runtime reflection, `Resources.getIdentifier`, or story-specific drawable lookup for artwork.
- Artwork should remain offline, original, semantic-color-based, and decorative by default unless a localized content description is genuinely required.
- Every production story requires a completed educational review file and review-index entry.
- No story may be marked production-ready without a final review status.
- Philosophical missions require at least two defensible positions.
- Perspective steps must be charitable and must not reveal the author's preferred answer.
- Counterexamples must test a principle rather than reveal a correct answer.
- Completion text must remain open; changing a view must not be praised over retaining a view.
- Story options must not disguise a correct answer or moral score.
- Artwork must not assign blame, shame a character, or imply the correct answer.
- Content-revision impact must be recorded in the review.
- Internal review does not equal formal educational certification.
- Future user studies require separate ethical and privacy review.
- Story content must not include correct-answer fields, scores, points, rewards, badges, profiles, or ranking.
- Populated worlds must use the generic manifest, repository, mission list, story renderer, and progress system rather than world-specific rendering.
- Friendship content must not request personal friendship disclosures.
- Loyalty must not be presented as blind agreement.
- Inclusion must not be presented as forced friendship.
- Social exclusion scenarios must remain fictional, low-risk, and age-appropriate.
- Database schema changes require explicit migrations and tests.
- Never use destructive Room migrations in production configuration.
- Room schemas must be exported and committed.
- Persisted user progress must use stable ASCII IDs only.
- Authored Persian story text must not be stored in progress tables.
- Do not store personal identifiers or free-text answers without explicit future privacy review.
- Backup and data-extraction rules must match privacy documentation.
- Clear-all local progress behavior must remain tested.

## Reporting Requirements

Every final response must include:

1. Summary
2. Changed files
3. Technical decisions
4. Verification performed
5. Known limitations
6. Suggested next task

Also report:

- Commands run and their results.
- Files changed.
- Any verification that could not be run.
- Any known risk or limitation.

Do not fabricate successful test results.

## Definition of Done

A task is done when:

- The requested files or behavior are implemented.
- The change is consistent with product, privacy, RTL, and accessibility principles.
- Relevant tests or validation are run, or the reason they could not be run is stated.
- No unrelated files are modified.
- The final response follows the required format.

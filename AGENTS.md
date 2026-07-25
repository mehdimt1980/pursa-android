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
- Avoid hard-coded user-facing strings; plan for localization resources.
- Use semantic names that describe product meaning, not temporary implementation details.
- Add tests for new logic.
- Keep content data separate from UI logic.
- Do not include copyrighted media unless permission and license details are documented.
- Never include real children's personal data.

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

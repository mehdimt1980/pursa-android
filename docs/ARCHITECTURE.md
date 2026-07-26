# Architecture

This document describes the architecture direction for `پرسا | Pursa`. The current scaffold includes a minimal Android app, a reusable Compose design system, and the first Navigation Compose shell.

## Initial Module Strategy

The MVP uses a single Android app module named `app`. Premature multi-module architecture is not allowed.

Future modularization may be considered only after real complexity appears, such as independent content tooling, reusable design-system libraries, or clearly separated feature ownership.

## Planned Stack

- Kotlin
- Jetpack Compose
- Material 3
- Gradle Kotlin DSL
- Gradle version catalog
- MVVM
- StateFlow
- Navigation Compose
- Room
- DataStore
- Hilt
- JUnit
- Compose UI tests
- GitHub Actions

Application ID: `org.pursa.app`

The current app includes a launchable Compose root, a Persian RTL welcome screen, Home, three foundational world entry points, World Detail, a minimal `Application` class, unit test source, and Compose UI test source. It intentionally does not include Room, DataStore, Hilt, Media3, backend access, analytics, settings, profiles, progress, or story content.

## Package Direction

Use a feature-oriented package structure. The current app keeps navigation in `org.pursa.app.navigation`, reusable UI in `org.pursa.app.designsystem`, and the first feature surfaces in `org.pursa.app.feature.home` and `org.pursa.app.feature.world`.

Each feature should keep responsibilities clear:

- UI: Compose screens, state rendering, user events, and accessibility semantics.
- Domain: use cases, validation, progress rules, and content interaction logic.
- Data: repositories, local persistence, JSON asset loading, Room DAOs, and DataStore preferences.

## Unidirectional Data Flow

Screens should render immutable UI state from ViewModels. User events should flow back to the ViewModel, which coordinates domain logic and repositories. State should be exposed through StateFlow.

## Offline-First Content

Core story content should be bundled with the app and usable without network access. Authored stories should be represented as JSON assets so educational content can be reviewed separately from UI code.

## Local Storage

Room should store structured local user progress and journal entries. DataStore should store lightweight preferences such as display options or completed onboarding flags.

The MVP should not sync data to a backend.

## Repository Interfaces

Use repository interfaces to separate feature logic from storage details. Repositories should make it possible to test domain behavior without Android framework dependencies where practical.

## Test Strategy

Planned test coverage should include:

- Unit tests for domain logic.
- Repository tests for content loading and persistence behavior.
- ViewModel tests for state transitions.
- Compose UI tests for critical screens.
- RTL, accessibility, and navigation checks.
- CI verification through GitHub Actions.

Current Phase 4 coverage includes unit tests for stable world IDs, world lookup, starter-question counts, and route construction, plus Compose UI tests for the initial Welcome destination, Welcome-to-Home navigation, Home world cards, World Detail navigation, back behavior, and an invalid world route fallback.

## Future Modularization

Possible future modules might include content tooling, a design-system module, or separated feature modules. This should happen only when it reduces demonstrated complexity.

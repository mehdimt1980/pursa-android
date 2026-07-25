# Architecture

This document describes the planned architecture for `پرسا | Pursa`. It does not define implementation code.

## Initial Module Strategy

The MVP should use a single Android app module. Premature multi-module architecture is not allowed.

Future modularization may be considered only after real complexity appears, such as independent content tooling, reusable design-system libraries, or clearly separated feature ownership.

## Planned Stack

- Kotlin
- Jetpack Compose
- Material 3
- Gradle Kotlin DSL
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

## Package Direction

Use a feature-oriented package structure. A future project might group code by product areas such as stories, reflection, progress, settings, and design system.

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

## Future Modularization

Possible future modules might include content tooling, a design-system module, or separated feature modules. This should happen only when it reduces demonstrated complexity.

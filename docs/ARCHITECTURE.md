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

The current app includes a launchable Compose root, a Persian RTL welcome screen, Home, three foundational world entry points, World Detail, four offline JSON-authored Truth missions, a minimal `Application` class, unit test source, and Compose UI test source. It intentionally does not include Room, DataStore, Hilt, Media3, backend access, analytics, settings, profiles, or persistent progress.

## Package Direction

Use a feature-oriented package structure. The current app keeps navigation in `org.pursa.app.navigation`, reusable UI in `org.pursa.app.designsystem`, and the first feature surfaces in `org.pursa.app.feature.home` and `org.pursa.app.feature.world`.

Phase 5 adds `org.pursa.app.content` for platform-neutral story models, local asset parsing, validation, repository access, and temporary story-session state. Mission list UI lives in `org.pursa.app.feature.missions`; fixed-order story rendering lives in `org.pursa.app.feature.story`.

Each feature should keep responsibilities clear:

- UI: Compose screens, state rendering, user events, and accessibility semantics.
- Domain: use cases, validation, progress rules, and content interaction logic.
- Data: repositories, local persistence, JSON asset loading, Room DAOs, and DataStore preferences.

## Unidirectional Data Flow

Screens should render immutable UI state from ViewModels. User events should flow back to the ViewModel, which coordinates domain logic and repositories. State should be exposed through StateFlow.

## Offline-First Content

Core story content should be bundled with the app and usable without network access. Authored stories should be represented as JSON assets so educational content can be reviewed separately from UI code.

The story asset structure is manifest-indexed:

```text
app/src/main/assets/content/
├── schema/story.schema.json
└── fa/
    ├── manifest.json
    └── stories/truth/truth_broken_vase.json
```

The manifest lists story IDs, world IDs, and asset paths. The repository reads the manifest rather than scanning assets, parses JSON with strict `kotlinx.serialization` settings, validates content through `StoryContentValidator`, and exposes structured `StoryContentResult` values: success, not found, invalid content, or read failure.

Phase 6 expands the Truth directory to four registered missions: `truth_broken_vase.json`, `truth_group_photo.json`, `truth_strange_news.json`, and `truth_friend_secret.json`. Manifest order is the display order for mission lists, so content authors can define a pedagogical sequence. The repository also validates duplicate story IDs and duplicate asset paths before exposing mission summaries.

Story steps are represented as a sealed model with exactly these supported types: `narrative`, `single_choice`, `reason_prompt`, `perspective`, `counterexample`, and `reflection`. Phase 5 stories advance in fixed authored order; branching, scripting, scoring, correct answers, downloads, and remote actions are out of scope.

## Local Storage

Room should store structured local user progress and journal entries. DataStore should store lightweight preferences such as display options or completed onboarding flags.

The MVP should not sync data to a backend.

Phase 5 does not persist mission completion or answers. `StorySessionReducer` keeps deterministic temporary state for the active story screen: current step, selected option IDs keyed by step ID, continuation eligibility, completion, and progress.

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

Current coverage includes unit tests for stable world IDs, route construction, JSON parsing, content validation, repository behavior, and story-session transitions, plus Compose UI tests for Welcome/Home/World Detail navigation and the first Truth mission flow.

## Future Modularization

Possible future modules might include content tooling, a design-system module, or separated feature modules. This should happen only when it reduces demonstrated complexity.

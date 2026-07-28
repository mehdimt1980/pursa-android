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

The current app includes a launchable Compose root, a Persian RTL welcome screen, Home, three foundational world entry points, World Detail, four offline JSON-authored Truth missions, local mission-progress persistence, a Settings/Data & Privacy screen, a minimal `Application` class, unit test source, and Compose UI test source. It intentionally does not include DataStore, Hilt, Media3, backend access, analytics, profiles, journal storage, or free-text progress.

## Package Direction

Use a feature-oriented package structure. The current app keeps navigation in `org.pursa.app.navigation`, reusable UI in `org.pursa.app.designsystem`, and the first feature surfaces in `org.pursa.app.feature.home` and `org.pursa.app.feature.world`.

Phase 5 adds `org.pursa.app.content` for platform-neutral story models, local asset parsing, validation, repository access, and story-session state. Phase 7 adds `org.pursa.app.progress` for Room-backed mission progress, saved sessions, selected option IDs, and repository mapping. Mission list UI lives in `org.pursa.app.feature.missions`; fixed-order story rendering lives in `org.pursa.app.feature.story`.

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

Each production story has a positive `contentRevision`. Saved active sessions store that revision and are restored only when it matches the current story. If the revision, current step, or selected option IDs are stale, the active session is cleared safely while completed history is preserved when present.

## Local Storage

Room stores structured local mission progress and resumable sessions. The version-1 schema has three normalized tables:

- `mission_progress`: one row per story ID with `NotStarted`, `InProgress`, or `Completed` represented through status rows where needed, plus local technical timestamps.
- `story_sessions`: one active session per story with current step index, content revision, session schema version, and update time.
- `story_answers`: selected option IDs keyed by story ID and step ID.

DataStore remains deferred because Phase 7 introduces no genuine small preference. If future display preferences or one-time notices need persistence, Preferences DataStore may be added without replacing Room for relational progress data.

The MVP should not sync data to a backend.

`StorySessionReducer` remains pure and deterministic. `StoryViewModel` coordinates content loading, saved-session restoration, autosave after meaningful state changes, completion, and repository errors around the reducer. Mission-list and settings screens use small ViewModels and `StateFlow`.

Room schema export is enabled through KSP arguments at `app/schemas`. Future database changes require explicit migrations, exported schema updates, and migration tests. Production database construction must not use destructive migration or main-thread queries.

Dependencies are created explicitly in `PursaAppContainer`: the Room database, content repository, and progress repository. No dependency-injection framework is used.

Android backup is disabled at the manifest level and backup/data-extraction XML excludes app data, so local progress is not silently backed up or transferred.

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

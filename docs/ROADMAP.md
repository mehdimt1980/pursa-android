# Roadmap

## Phase 11 Status

Phase 11 is implemented in the development build when local verification passes: refreshed semantic palette, warm screen canvases, distinct world identities, Compose-drawn decorative motifs, Home/Welcome/World/Story/Summary/Journal/Settings visual refreshes, contrast tests, CI release assembly, instrumentation-test APK assembly, and quality documentation.

Still out of scope: external illustrations, audio, formal educational evaluation, formal accessibility certification, release signing, public distribution, store listing, dark theme, dynamic color, accounts, cloud features, analytics, and Phase 12 features.

This roadmap describes planned phases. It does not claim that any app feature has already been implemented.

## Phase 0: Repository and Product Documentation

Objective: Establish product, contribution, privacy, architecture, and governance foundations.

Main deliverables:

- README
- Contribution rules
- Agent rules
- Product documentation
- Architecture direction
- Content guide
- Privacy specification
- Roadmap
- Decision log

Exit criteria:

- Required documentation files exist.
- Project principles are clear.
- No Android implementation has been generated.

## Phase 1: Android Project Scaffold

Objective: Create the initial Android project structure.

Main deliverables:

- Kotlin Android project.
- Application ID `org.pursa.app`.
- Gradle Kotlin DSL setup.
- Basic Compose entry point.

Exit criteria:

- Project opens in Android Studio.
- Debug build can be assembled when the Android SDK and Gradle dependencies are available.
- No prohibited services are added.

Status: Implemented as an initial scaffold with a single Compose activity, Persian RTL welcome screen, local unit test source, and Compose UI test source. Build verification depends on the local Android SDK and dependency resolution environment.

## Phase 2: CI and Build Verification

Objective: Add automated verification.

Main deliverables:

- GitHub Actions workflow.
- Unit test command.
- Lint command.
- Debug build command.

Exit criteria:

- CI runs on pull requests.
- Build and test results are visible in GitHub.

Status: Workflow implemented in the repository. It runs lint, local unit tests, and debug assembly on pushes to `main`, pull requests targeting `main`, and manual dispatch. Local parser-backed YAML validation was not available in the current environment; successful execution on GitHub is not yet confirmed until the workflow runs remotely.

## Phase 3: Design System

Objective: Establish reusable visual and interaction foundations.

Main deliverables:

- Theme.
- Typography.
- Spacing and shape tokens.
- Reusable Compose components.
- RTL verification examples.

Exit criteria:

- Core UI components are reusable.
- Accessibility and RTL requirements are documented in code review.

Status: Implemented as a foundational Compose Material 3 design system with semantic tokens, centralized spacing and sizes, reusable buttons, cards, chips, progress indicators, messages, a top bar, RTL previews, a preview-only showcase, and welcome screen migration.

## Phase 4: Navigation and Foundational Screens

Objective: Build the first navigable app shell.

Main deliverables:

- Home screen.
- World selection.
- Navigation Compose setup.
- World detail foundation.

Exit criteria:

- Users can navigate core screens.
- Screens work in RTL.

Status: Implemented as the first navigable app shell: Welcome starts the app, Continue opens Home and removes Welcome from the back stack, Home lists the three MVP worlds, and each world can open a foundational detail screen with starter questions. Profiles remain deferred.

## Phase 5: Story Engine

Objective: Load and present authored story content.

Main deliverables:

- JSON story format.
- Local asset loader.
- Story state model.
- Question and activity rendering.

Exit criteria:

- A sample local story can be loaded offline.
- Story steps render correctly.
- Story logic has tests.

Status: Implemented as a first offline story-content engine with JSON Schema, manifest-indexed Persian assets, strict kotlinx.serialization parsing, runtime validation, a local repository, fixed-order story UI, temporary in-memory interaction state, and unit/UI test coverage. Full Justice missions, Friendship missions, persistence, journal, Room, DataStore, audio, and illustrations remain incomplete.

## Phase 6: First Complete Truth World

Objective: Deliver the first complete reviewed content world for Truth and lying.

Main deliverables:

- Four Truth missions.
- Open questions.
- Reasoning activity.
- Counterexample or alternative viewpoint prompt.
- Reflection prompt.

Exit criteria:

- Content passes review checklist.
- Story works offline.
- Basic accessibility and RTL checks pass.

Status: Implemented with four manifest-registered offline Truth missions: `truth_broken_vase`, `truth_group_photo`, `truth_strange_news`, and `truth_friend_secret`. Each mission uses JSON-authored Persian content, fixed-order interactive reflection, reasons, perspective-taking, counterexamples, and completion reflection. Parser, validation, repository, production inventory, and UI tests exist. Justice content, Friendship content, persistent progress, journal, Room, DataStore, audio, illustrations, parent mode, and school mode remain incomplete.

## Phase 7: Local Progress Persistence

Objective: Add privacy-preserving local mission progress and session resumption.

Main deliverables:

- Room-backed mission completion.
- Resumable in-progress story sessions.
- Local selected-answer ID persistence.
- Replay for completed missions.
- Clear-all local progress controls.

Exit criteria:

- Progress is stored locally.
- Sessions restore safely.
- Local data can be cleared.
- No network is required.

Status: Implemented with Room-backed local mission progress, resumable active sessions, selected option ID persistence, completion recording, replay support, content revision compatibility, explicit app-container construction, Settings/Data & Privacy clear-all controls, backup exclusion, exported Room schema, and DAO/repository test coverage. Journal, free-text reflection, Justice content, Friendship content, audio, illustrations, parent mode, school mode, accounts, and cloud sync remain incomplete.

## Phase 8: Second Complete Justice World

Objective: Deliver the second complete reviewed content world for Justice and fairness.

Main deliverables:

- Four Justice missions.
- Open questions.
- Reasoning activity.
- Perspective-taking.
- Counterexample or changed-condition prompt.
- Reflection prompt.

Exit criteria:

- Content passes review checklist.
- Story works offline.
- Basic parser, repository, inventory, RTL, safety, and UI behavior checks are covered.

Status: Implemented with four manifest-registered offline Justice missions: `justice_last_cake`, `justice_class_representative`, `justice_playground_rule`, and `justice_team_prize`. Each mission uses JSON-authored Persian content, fixed-order interactive reflection, reasons, perspective-taking, counterexamples, and completion reflection. Parser, validation, repository, production inventory, and Justice UI-flow tests exist. Friendship content, journal, free-text reflection, audio, illustrations, parent mode, school mode, accounts, cloud sync, and full release-candidate QA remain incomplete.

## Phase 9: Third Complete Friendship World

Objective: Deliver the third complete reviewed content world for Friendship and loyalty.

Main deliverables:

- Four Friendship missions.
- Open questions.
- Reasoning activity.
- Perspective-taking.
- Counterexample or changed-condition prompt.
- Reflection prompt.

Exit criteria:

- Content passes review checklist.
- Story works offline.
- Basic parser, repository, inventory, persistence, RTL, safety, and UI behavior checks are covered.

Status: Implemented with four manifest-registered offline Friendship missions: `friendship_new_friend`, `friendship_difficult_promise`, `friendship_whose_side`, and `friendship_game_without_them`. Each mission uses JSON-authored Persian content, fixed-order interactive reflection, reasons, perspective-taking, counterexamples, and completion reflection. Parser, validation, repository, production inventory, persistence, and Friendship UI-flow tests exist. Journal, free-text reflection, audio, illustrations, accessibility audit, parent mode, teacher mode, accounts, cloud sync, release hardening, signed release, and public distribution remain incomplete.

## Phase 10: Accessibility and Quality Assurance

Objective: Prepare for a trustworthy MVP release candidate.

Main deliverables:

- Accessibility review.
- RTL review.
- Device-size checks.
- Content safety review.
- Privacy review.

Exit criteria:

- Major accessibility, RTL, privacy, and content issues are addressed or documented.

## Phase 11: Signed GitHub Release

Objective: Publish a signed open-source release through GitHub.

Main deliverables:

- Release signing process.
- Release notes.
- APK or AAB artifact as appropriate.
- Source archive.

Exit criteria:

- Release artifacts are signed.
- License and privacy documentation are included.
- Release does not claim more than has been verified.

## Phase 12: Offline Illustration System

Objective: Add a local, repeatable illustration system for worlds, stories, and supporting states.

Main deliverables:

- Central artwork registry with stable keys.
- Compose-drawn original offline artwork for three worlds, twelve stories, completion, empty journal, and fallback states.
- Story JSON `artworkKey` fields.
- Contributor workflow for future story artwork.
- Registry and production-content artwork tests.

Exit criteria:

- No external assets, image-loading dependency, or network permission is introduced.
- All production story artwork keys resolve through the registry.
- Artwork is decorative by default and accessible screen text remains the source of meaning.

Status: Implemented in source; final Gradle verification depends on a local Android SDK or CI environment.

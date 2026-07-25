# Roadmap

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

## Phase 4: Navigation and Foundational Screens

Objective: Build the first navigable app shell.

Main deliverables:

- Home screen.
- World selection.
- Settings.
- About/privacy screen.
- Navigation Compose setup.

Exit criteria:

- Users can navigate core screens.
- Screens work in RTL.

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

## Phase 6: First Complete Story

Objective: Deliver one complete reviewed story experience.

Main deliverables:

- One story in an initial world.
- Open questions.
- Reasoning activity.
- Counterexample or alternative viewpoint prompt.
- Reflection prompt.

Exit criteria:

- Content passes review checklist.
- Story works offline.
- Basic accessibility and RTL checks pass.

## Phase 7: Three MVP Worlds

Objective: Provide initial content coverage.

Main deliverables:

- Truth and lying world.
- Justice and fairness world.
- Friendship and loyalty world.
- Local progress across stories.

Exit criteria:

- Each world has reviewed content.
- Progress is stored locally.
- No network is required.

## Phase 8: Accessibility and Quality Assurance

Objective: Prepare for a trustworthy MVP release candidate.

Main deliverables:

- Accessibility review.
- RTL review.
- Device-size checks.
- Content safety review.
- Privacy review.

Exit criteria:

- Major accessibility, RTL, privacy, and content issues are addressed or documented.

## Phase 9: Signed GitHub Release

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

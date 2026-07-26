# Decisions

This is a lightweight architecture decision log for `پرسا | Pursa`.

## ADR-001: Kotlin Instead of Flutter

Status: Accepted

Context: Pursa is planned as an Android-first app with strong platform integration and long-term maintainability.

Decision: Use Kotlin for the Android application.

Consequences: Contributors should follow native Android practices. Cross-platform support is not part of the MVP.

## ADR-002: Jetpack Compose Instead of XML Layouts

Status: Accepted

Context: The app needs a modern UI stack with reusable components and strong state-driven rendering.

Decision: Use Jetpack Compose for UI.

Consequences: UI work should use Compose and Material 3 rather than XML layouts.

## ADR-003: Android-Only Initial Release

Status: Accepted

Context: The initial product focuses on a practical MVP for Android users.

Decision: Target Android only for the initial release.

Consequences: iOS, web, and desktop apps are outside the MVP scope.

## ADR-004: Persian-First and RTL-First

Status: Accepted

Context: Pursa is designed primarily for Persian-speaking children in Iran.

Decision: Treat Persian language and RTL layout as first-class requirements.

Consequences: UI, typography, content, and QA must account for RTL from the beginning.

## ADR-005: Offline-First

Status: Accepted

Context: The app should be usable without registration or internet access.

Decision: Core content and progress features must work offline.

Consequences: Story content should be bundled locally, and MVP progress should be stored on device.

## ADR-006: No Backend in MVP

Status: Accepted

Context: A backend would increase privacy, cost, maintenance, and safety complexity.

Decision: Do not include a backend in the MVP.

Consequences: No cloud sync, accounts, remote content, or server-dependent features in the MVP.

## ADR-007: No AI Inside the MVP

Status: Accepted

Context: Online or open-ended AI features create child-safety, privacy, and moderation risks.

Decision: Do not include AI features inside the MVP.

Consequences: Content should be authored, reviewed, and bundled locally.

## ADR-008: No Registration

Status: Accepted

Context: The app should be easy to use and privacy-preserving.

Decision: Do not require registration or account creation.

Consequences: Avoid collecting names, emails, phone numbers, or identity data.

## ADR-009: JSON-Authored Story Content

Status: Accepted

Context: Educational content needs reviewability and separation from UI implementation.

Decision: Store authored story content as local JSON assets.

Consequences: JSON schemas or validation should be introduced when implementation begins.

## ADR-010: Room for Structured Local User Progress

Status: Accepted

Context: Progress and journal state require structured local persistence.

Decision: Use Room for structured local progress and journal entries.

Consequences: Persistence logic should be tested, privacy-preserving, and local-only.

## ADR-011: GitHub Actions for CI and APK Builds

Status: Accepted

Context: Open-source contributors need repeatable build and test verification.

Decision: Use GitHub Actions for CI and release build automation when the Android project exists.

Consequences: CI should verify tests, lint, and build outputs without claiming release readiness prematurely.

## ADR-012: Apache-2.0 for Source Code

Status: Accepted

Context: The project needs a permissive open-source license suitable for collaboration.

Decision: License source code under Apache License 2.0.

Consequences: Contributors should ensure new source files and assets are compatible with Apache-2.0 or have clearly documented licenses.

## ADR-013: Initial Android Build Toolchain

Status: Accepted

Context: Phase 1 needs a stable native Android scaffold using Compose, Gradle Kotlin DSL, and a version catalog.

Decision: Use Android Gradle Plugin 9.2.0, Gradle 9.4.1, Kotlin 2.3.21, the Kotlin Compose compiler plugin, Compose BOM 2026.06.00, compileSdk 36, targetSdk 36, and minSdk 26.

Consequences: The scaffold follows the current stable Android 16 toolchain direction. Local verification requires an installed Android SDK platform 36 and dependency resolution access.

## ADR-014: Initial GitHub Actions CI

Status: Accepted

Context: Phase 2 needs repeatable validation for the Android scaffold without release signing, publishing, or emulator infrastructure.

Decision: Use GitHub Actions with one Ubuntu job, Java 17 through `actions/setup-java`, and `gradle/actions/setup-gradle` for Gradle cache configuration, wrapper validation, and build summaries. The required checks are Android lint, local unit tests, and debug APK assembly. The workflow uploads temporary debug APK and CI report artifacts.

Consequences: Pull requests get early feedback on lint, unit tests, and compilation. Instrumentation tests, release signing, publishing, dependency review, and emulator-based validation remain deferred.

## ADR-015: Semantic Compose Design Tokens

Status: Accepted

Context: Phase 3 needs a reusable design foundation without introducing feature architecture or third-party UI dependencies.

Decision: Use Material 3 theme roles for color, typography, and shapes, plus Pursa-specific semantic colors for product concepts such as curiosity, discovery, reflection, success, and warning.

Consequences: Components refer to semantic meaning instead of raw palette names. The palette can evolve without rewriting feature screens.

## ADR-016: Platform Font Fallback for Initial Typography

Status: Accepted

Context: Pursa needs readable Persian typography without adding licensing risk or asset complexity.

Decision: Use Android platform-safe font fallback and tune Material typography roles for Persian line height and readable sizes.

Consequences: No custom font is bundled or downloaded in the initial design system. A future font decision must include license review.

## ADR-017: Light Theme Only and Dynamic Color Disabled

Status: Accepted

Context: The MVP needs a deterministic visual identity and a small design surface.

Decision: Implement only the light Pursa theme for now and keep dynamic color disabled.

Consequences: Dark theme and system-derived color are deferred until the product has enough surfaces for proper QA.

## ADR-018: Centralized Spacing Through CompositionLocal

Status: Accepted

Context: Compose components need consistent spacing, touch targets, and content width guidance without scattering dimensions through screens.

Decision: Provide spacing and size tokens through CompositionLocal-backed Pursa theme accessors.

Consequences: Screens and components can share a restrained scale while still allowing local one-off values where truly component-specific.

## ADR-019: Reusable Components Before Feature Screens

Status: Accepted

Context: Pursa will later add stories, worlds, reflection, settings, and progress surfaces.

Decision: Build reusable buttons, cards, chips, progress, message, and top-bar components before expanding feature screens.

Consequences: Phase 4 screens can compose existing components instead of inventing local UI patterns.

## ADR-020: No Third-Party UI Library

Status: Accepted

Context: The design system should stay small, auditable, offline-friendly, and aligned with the existing Compose Material 3 stack.

Decision: Do not add third-party UI libraries for the initial design system.

Consequences: The project avoids extra dependency surface and keeps UI behavior within official Compose and Material 3 APIs.

## ADR-021: Navigation Compose for the App Shell

Status: Accepted

Context: Phase 4 needs a small navigable shell while keeping the MVP single-module and offline-first.

Decision: Use official AndroidX Navigation Compose with one root `NavHost` and one root `NavController`. Routes are centralized as `welcome`, `home`, and `world/{worldId}`. The initial world IDs are `truth`, `justice`, and `friendship`.

Consequences: Feature screens receive immutable display data and callbacks instead of owning navigation controllers. Welcome is removed from the back stack when the user enters Home. Settings, progress, profiles, and story navigation remain deferred until their phases.

## ADR-022: Manifest-Indexed JSON Story Assets

Status: Accepted

Context: Phase 5 needs reviewed offline stories without hard-coding authored text in Kotlin.

Decision: Store authored Persian story text as one JSON file per story under `app/src/main/assets/content/fa/`, indexed by a local manifest. Runtime UI labels remain Android string resources.

Consequences: New stories require manifest registration, validation, and tests. The repository reads known entries rather than scanning assets unpredictably.

## ADR-023: kotlinx.serialization for Local JSON Parsing

Status: Accepted

Context: The story engine needs stable local JSON parsing compatible with the Kotlin toolchain.

Decision: Use the official Kotlin serialization plugin and `kotlinx-serialization-json` for local asset parsing with strict unknown-key handling.

Consequences: Malformed or structurally invalid authored content returns structured failures. No Gson, Moshi, Jackson, networking, or remote content layer is introduced.

## ADR-024: Fixed-Order Sealed Story Steps

Status: Accepted

Context: The first mission should support philosophical interaction without branching or scripting complexity.

Decision: Represent story steps as a sealed hierarchy supporting exactly `narrative`, `single_choice`, `reason_prompt`, `perspective`, `counterexample`, and `reflection`, rendered in authored order.

Consequences: The UI can use exhaustive handling and avoid unsafe casts. Branching, conditional jumps, scripts, scoring, and correct-answer feedback are deferred and not part of Phase 5.

## ADR-025: Runtime Validation Before Exposure

Status: Accepted

Context: Content files are authored separately from code and must fail safely.

Decision: Validate parsed stories before exposing them as playable missions. Validation checks IDs, world IDs, age ranges, required text, option counts, uniqueness, and completion reflection.

Consequences: Invalid content is not shown as a normal mission. Direct navigation to invalid or missing story IDs shows calm UI errors rather than stack traces.

## ADR-026: Temporary In-Memory Story Sessions

Status: Accepted

Context: Phase 5 needs interactive story state but must not start persistence, profiles, journals, or child-authored text.

Decision: Keep current step, selected option IDs, continuation eligibility, completion, and progress in deterministic in-memory state while the story screen is open.

Consequences: Completion and answers are not saved after the screen/process is gone. Room, DataStore, persistent progress, and journals remain future work.

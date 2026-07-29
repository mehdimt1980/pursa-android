# Decisions

## Phase 11 Visual Identity Decisions

- Use a warm non-white canvas as the default app background; reserve pure white for intentionally elevated reading surfaces.
- Keep Pursa violet as the global brand and Reflection Journal identity.
- Give Truth teal, Justice amber, and Friendship coral distinct semantic palettes.
- Keep colors semantic; feature code should not introduce raw hexadecimal palette values.
- Use original Compose-drawn abstract motifs rather than external illustrations, downloaded SVGs, mascots, or bitmap textures.
- Do not add a custom font in Phase 11; preserve platform font fallback and tune Material typography for Persian readability.
- Keep story-reading surfaces calm, high-contrast, and width-constrained.
- Selected states must combine semantics, text, border, surface, and accent, not color alone.
- Keep the app light-theme-only and leave dynamic color disabled.
- Compile instrumentation-test APKs and release APKs in CI; emulator execution remains a separate device-backed step.
- Do not add screenshot-testing dependencies or external visual assets in this phase.

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

## ADR-027: Four-Mission First Truth World

Status: Accepted

Context: Phase 6 expands Truth and lying from one sample mission into the first coherent production content world.

Decision: Ship exactly four Truth missions for this phase: broken vase, group photo, strange news, and friend's secret.

Consequences: The first content world now covers personal responsibility, digital representation, source reliability, and low-risk secrecy without starting Justice or Friendship content.

## ADR-028: Manifest Order Defines Learning Order

Status: Accepted

Context: The Truth missions are intended to move from familiar personal situations toward more socially complex questions.

Decision: Use the manifest story order as the child-facing mission order.

Consequences: Contributors must preserve pedagogical order intentionally instead of sorting production missions alphabetically.

## ADR-029: Content-First Validation Before Persistence

Status: Accepted

Context: Multiple authored missions should prove the content engine before local progress or journals are introduced.

Decision: Keep validation, parsing, inventory tests, and in-memory completion as the Phase 6 focus.

Consequences: Room, DataStore, persistent completion, and journal storage remain deferred until the content engine is exercised by a broader mission set.

## ADR-030: Low-Risk Digital Ethics and Secret Scenarios

Status: Accepted

Context: Truth content should explore privacy, consent, uncertainty, loyalty, and fairness without exposing children to severe harm scenarios.

Decision: Use familiar low-risk school, class group, photo, news, and competition premises.

Consequences: Serious-harm secrets, real current events, political references, brand names, and sensitive disclosure prompts remain out of production story content unless a future dedicated safety review approves them.

## ADR-031: No New Story Step Types in Phase 6

Status: Accepted

Context: The existing fixed-order story engine already supports narrative, choice, reasons, perspective, counterexample, and reflection.

Decision: Author all Phase 6 missions with the existing six step types.

Consequences: No branching, scripting, free text, custom evidence-comparison type, or story-ID-specific rendering is introduced.

## ADR-032: Manifest-Level Inventory Validation

Status: Accepted

Context: Once multiple stories are registered, duplicate story IDs or duplicate asset paths can make repository behavior ambiguous.

Decision: Validate manifest story ID and asset path uniqueness before exposing story summaries.

Consequences: Invalid production inventory fails safely through structured invalid-content results, and tests cover duplicate manifest entries.

## ADR-033: Room for Structured Local Progress

Status: Accepted

Context: Phase 7 needs local mission completion and resumable sessions without accounts or backend storage.

Decision: Use AndroidX Room for mission progress, active sessions, and selected option IDs.

Consequences: Progress remains structured, queryable, local to the app installation, and testable with in-memory databases.

## ADR-034: DataStore Deferred Until a Real Preference Exists

Status: Accepted

Context: Phase 7 allows Preferences DataStore only for genuine small preferences.

Decision: Do not add DataStore in Phase 7 because no preference is required for the implemented data controls.

Consequences: Room remains the only new persistence technology. Future small app preferences may use Preferences DataStore without replacing Room progress tables.

## ADR-035: Explicit App Container Without DI Framework

Status: Accepted

Context: The app needs one database instance per process but should not add Hilt, Koin, Dagger, or a service locator.

Decision: Construct the database and repositories in `PursaAppContainer`, owned by `PursaApp`.

Consequences: Dependency creation stays explicit and testable while avoiding a broad architecture rewrite.

## ADR-036: Three Calm Progress States

Status: Accepted

Context: Mission progress should support resumption and replay without gamification.

Decision: Represent user-facing status as `NotStarted`, `InProgress`, and `Completed`.

Consequences: No scores, ranks, streaks, grades, mastery states, or failure states are introduced.

## ADR-037: Replay Preserves Completion History

Status: Accepted

Context: A child may revisit a completed mission without losing the historical fact that it was completed.

Decision: Starting a completed mission creates a new active session and preserves the previous completion timestamp until completion happens again.

Consequences: Completed history survives replay, while mission cards can still show an active in-progress replay session.

## ADR-038: Active Sessions Removed on Completion

Status: Accepted

Context: Completed missions should not keep stale resumable answer rows.

Decision: Marking completion deletes active session and saved answer rows transactionally.

Consequences: Completion state remains minimal and does not retain detailed interaction rows beyond the completed progress record.

## ADR-039: Stable ID-Only Answer Persistence

Status: Accepted

Context: Saved answers are needed only to restore selection state.

Decision: Store story IDs, step IDs, and selected option IDs, not Persian labels, authored story text, free text, or inferred meaning.

Consequences: Data minimization is preserved, and restoration validates IDs against current content before use.

## ADR-040: Story Content Revisions for Session Compatibility

Status: Accepted

Context: Authored story step structure may change after a session has been saved.

Decision: Add positive integer `contentRevision` to production stories and save it with active sessions.

Consequences: Mismatched revisions clear obsolete active sessions safely while preserving completed history where applicable.

## ADR-041: Exported Room Schemas and No Destructive Migration

Status: Accepted

Context: Local progress databases need reliable future migration discipline.

Decision: Enable Room schema export, commit versioned schema files, and avoid destructive migration APIs.

Consequences: Future schema changes require explicit migrations and migration tests instead of silent data loss.

## ADR-042: Local Backup Disabled for Progress Data

Status: Accepted

Context: Privacy documentation promises local-only progress without cloud sync.

Decision: Keep `android:allowBackup="false"` and exclude app data in backup and data-extraction rules.

Consequences: Mission progress is not silently backed up to cloud storage or transferred device-to-device by Android backup.

## ADR-043: Clear-All Local Progress Control

Status: Accepted

Context: Families need a simple way to remove stored progress from the device.

Decision: Add a Settings/Data & Privacy screen with confirmed clear-all local progress.

Consequences: The app can delete progress, active sessions, and selected answer rows without deleting packaged story assets.

## ADR-044: Four-Mission First Justice World

Status: Accepted

Context: Phase 8 expands Justice and fairness from a placeholder world into the second coherent production content world.

Decision: Ship exactly four Justice missions for this phase: last cake, class representative, playground rule, and team prize.

Consequences: The second content world now covers sharing, need, effort, representation, majority and minority concerns, school rules, safety, authority, contribution, and circumstances without starting Friendship content.

## ADR-045: No New Story Step Types in Phase 8

Status: Accepted

Context: The existing fixed-order story engine already supports the needed Justice interactions.

Decision: Author all Phase 8 Justice missions with the existing six step types.

Consequences: No branching, scoring, free text, custom rule-debate type, or Justice-specific production rendering is introduced.

## ADR-046: Four-Mission Friendship World Scope

Status: Accepted

Context: Phase 9 completes the third initial philosophical content world.

Decision: Ship exactly four Friendship missions for this phase: new friend, difficult promise, whose side, and game without them.

Consequences: The initial content set now contains twelve manifest-registered stories across Truth, Justice, and Friendship without adding journal, media, account, backend, or release features.

## ADR-047: Friendship Pedagogical Order

Status: Accepted

Context: Friendship questions move from first belonging to more complex group responsibility.

Decision: Preserve manifest order from beginning friendship, to promises, to conflict, to group inclusion.

Consequences: The app uses one manifest-defined learning sequence instead of alphabetical ordering or story-specific navigation.

## ADR-048: No Friendship-Specific Rendering

Status: Accepted

Context: The existing story engine, repository, mission list, and Room progress model are world-generic.

Decision: Serve Friendship stories through the same manifest, parser, validator, repository, mission cards, story renderer, and progress tables as Truth and Justice.

Consequences: No Friendship-specific UI component, repository branch, persistence table, or step type is introduced.

## ADR-049: Low-Risk Fictional Friendship Scenarios

Status: Accepted

Context: Friendship content can feel personal for children.

Decision: Keep Friendship missions fictional, third-person, emotionally low-risk, and free of personal disclosure prompts.

Consequences: The content avoids bullying, humiliation, dangerous secrecy, personal names entered by children, and requests for private friendship history.

## ADR-050: Support Is Not Blind Agreement

Status: Accepted

Context: Loyalty is philosophically important but can become unsafe or unfair if treated as automatic agreement.

Decision: Friendship stories distinguish support from agreement and inclusion from forced closeness.

Consequences: Missions may explore loyalty, correction, boundaries, repeated exclusion, compromise, and respectful distance without presenting a single rule for all friendships.

## ADR-051: Content Completion Before Journal and Media

Status: Accepted

Context: The initial three-world content set should be stable before expanding interaction modes.

Decision: Complete the twelve-story content inventory before adding journal storage, free-text reflection, audio, illustrations, or release automation.

Consequences: The app remains offline, low-risk, ID-only for progress persistence, and focused on fixed-order authored inquiry.

## ADR-052: Compose-Drawn Offline Artwork

Status: Accepted

Context: Pursa needs original story and world artwork without adding external assets, licensing risk, downloads, or a runtime image pipeline.

Decision: Implement Phase 12 artwork as local Compose-drawn vector geometry in the design system.

Consequences: The app remains fully offline, artwork is deterministic and theme-aware, and no image-loading dependency, remote URL, storage permission, or media permission is required.

## ADR-053: Stable Artwork Keys in Story Content

Status: Accepted

Context: Story content should remain platform-neutral and reviewable.

Decision: Store a stable `artworkKey` in story JSON and resolve it through `PursaArtworkRegistry`.

Consequences: Content does not include Android resource IDs, and UI code avoids reflection, `Resources.getIdentifier`, and story-specific drawable lookup.

## ADR-054: Internal Educational Review Before Release Engineering

Status: Accepted

Context: Technical validity does not prove educational readiness.

Decision: Review all production missions internally for philosophical tension, P4C flow, Persian language, child safety, inclusion, and artwork alignment before beginning release engineering.

Consequences: Phase 13 adds repository review artifacts and tests, not runtime features.

## ADR-055: One Review Report Per Production Story

Status: Accepted

Context: Future contributors need traceable production-readiness evidence.

Decision: Store one Markdown review report per production story under `content/reviews/` and maintain a review index.

Consequences: Review files stay outside Android assets and are not surfaced to children.

## ADR-056: Open-Ended Philosophy Over Moral Instruction

Status: Accepted

Context: Pursa should not become a morality quiz or behavior-correction tool.

Decision: Require at least two defensible positions, charitable perspectives, meaningful counterexamples, and open completion text.

Consequences: Stories may invite disagreement and uncertainty without scoring or hidden correct answers.

## ADR-057: No Automated Philosophical Scoring

Status: Accepted

Context: Automated scoring would create false precision and could contradict the product's open-ended purpose.

Decision: Tests check inventory, structure, identifiers, and required review files only. They do not score philosophical depth, safety, maturity, or correctness.

Consequences: Human review remains documented separately, and no AI/NLP scoring dependency is introduced.

## ADR-058: Editorial Revision Preserves Stable IDs Unless Compatibility Requires Change

Status: Accepted

Context: Room sessions and Reflection Journal records depend on stable story, step, option, and content-revision compatibility.

Decision: Preserve stable IDs during editorial review and increment `contentRevision` only when compatibility is affected.

Consequences: Phase 13 records no content-revision changes because no story JSON text or IDs were changed.

## ADR-059: Artwork Reviewed With Content

Status: Accepted

Context: Illustration can bias a child's judgment even when text is open-ended.

Decision: Include artwork alignment, neutrality, blame-cue, and text-image contradiction review in each mission review.

Consequences: No separate runtime artwork-review system is introduced.

## ADR-060: User Pilot Protocol Separate From Production App

Status: Accepted

Context: Child user research requires privacy and safeguarding discipline.

Decision: Document a future pilot protocol outside the app and prohibit personal disclosure, identifiers, recordings, analytics, or upload behavior in production.

Consequences: Internal review is not formal certification, and future user studies require separate ethical and privacy review.

## ADR-061: Tracked Semantic Release Version Source

Status: Accepted

Context: Release builds need deterministic Android `versionCode` and `versionName` values that are easy to review.

Decision: Store `VERSION_CODE` and `VERSION_NAME` in tracked `version.properties`, use semantic `MAJOR.MINOR.PATCH` names, and use `v<VERSION_NAME>` Git tags.

Consequences: Version changes are explicit in diffs. `VERSION_CODE` must increase manually for official releases.

## ADR-062: Ordinary CI Separate From Official Release

Status: Accepted

Context: Pull requests and branch pushes should not need production signing secrets or create public release assets.

Decision: Keep ordinary CI secret-free and add a separate release workflow for signed dry runs and draft GitHub Releases.

Consequences: Development artifacts remain non-production. Official publication only happens through deliberate release triggers.

## ADR-063: Production Keystore Outside Repository

Status: Accepted

Context: Android signing keys are long-lived trust anchors.

Decision: Do not create, commit, or store production keystores in the repository. Use protected GitHub environment secrets and temporary runner files for official signing.

Consequences: Maintainers must manage secure offline backups and secret rotation procedures.

## ADR-064: No Debug-Signing Fallback

Status: Accepted

Context: Accidentally publishing debug-signed or unsigned artifacts would damage update trust.

Decision: Official release mode fails closed unless all signing inputs are present and never falls back to debug signing.

Consequences: Normal local release verification remains unsigned, while official release paths require complete secrets.

## ADR-065: Mandatory Release Metadata And Allowlist

Status: Accepted

Context: Users and maintainers need verifiable, auditable release assets.

Decision: Official releases must include signed APK, signed AAB, SHA-256 checksums, SBOM, license report, build information, and release notes. Release staging rejects unexpected files.

Consequences: Public assets are deterministic and reviewable before publication.

## ADR-066: Draft-First GitHub Releases And Immutable Assets

Status: Accepted

Context: Maintainers need a final human review before public publication.

Decision: The release workflow creates draft GitHub Releases after validation and signing. Published release tags and assets are immutable except documented emergency recovery.

Consequences: Google Play upload, Play App Signing, store listing, artifact attestations, and public launch remain separate future decisions.

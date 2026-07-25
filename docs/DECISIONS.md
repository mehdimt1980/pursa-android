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

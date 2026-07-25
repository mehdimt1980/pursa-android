# پرسا | Pursa

Pursa is a free and open-source, offline-first Android app for helping Iranian children practice philosophical thinking through stories, questions, and reflection.

پرسا یک برنامه آزاد، رایگان و آفلاین برای کودکان فارسی‌زبان است که به آن‌ها کمک می‌کند پرسش‌های معنادار بپرسند، دلیل بیاورند، دیدگاه‌های متفاوت را ببینند و با احترام گفت‌وگو کنند.

## Project Status

Pursa is in early implementation. The repository now contains an initial Android scaffold, but no production app, signed release build, or public download exists yet.

## Continuous Integration

Pushes to `main` and pull requests targeting `main` run Android lint, local unit tests, and a debug build through GitHub Actions. Successful runs upload a temporary debug APK artifact for development review.

The debug APK artifact is an internal development build, not a stable public release. Instrumentation tests are not executed in CI yet because no emulator is configured in Phase 2.

## Core Principles

- Completely free for users.
- Open source.
- Offline-first.
- Usable without registration or internet access.
- No advertisements, in-app purchases, behavioral analytics, or social networking.
- Privacy-preserving and safe for children.
- Persian-first and fully RTL.
- Accessible and visually polished.
- Based on open-ended philosophical inquiry rather than memorization.

## Intended Audience

Pursa is designed primarily for children in Iran, approximately ages 8-12, and for families or educators who want to encourage careful, creative, collaborative, and critical thinking.

## Initial Content Worlds

1. Truth and lying
2. Justice and fairness
3. Friendship and loyalty

The app must not teach philosophy as memorization of philosophers, schools, dates, or definitions.

## Technology Direction

The planned Android application ID is `org.pursa.app`.

The planned stack is:

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
- Media3 when audio is introduced
- JUnit
- Compose UI tests
- GitHub Actions

## Repository Structure

```text
pursa-android/
├── README.md
├── AGENTS.md
├── CONTRIBUTING.md
├── CODE_OF_CONDUCT.md
├── LICENSE
├── .gitignore
├── settings.gradle.kts
├── build.gradle.kts
├── gradle.properties
├── gradle/
│   └── libs.versions.toml
├── app/
│   ├── build.gradle.kts
│   └── src/
├── .github/
│   ├── workflows/
│   │   └── android-ci.yml
│   ├── pull_request_template.md
│   └── ISSUE_TEMPLATE/
│       ├── bug_report.yml
│       ├── feature_request.yml
│       ├── content_proposal.yml
│       └── config.yml
├── docs/
│   ├── PRODUCT.md
│   ├── ARCHITECTURE.md
│   ├── CONTENT_GUIDE.md
│   ├── DESIGN_SYSTEM.md
│   ├── PRIVACY.md
│   ├── ROADMAP.md
│   └── DECISIONS.md
```

## Contribution Entry Points

- Read [CONTRIBUTING.md](CONTRIBUTING.md) before proposing changes.
- Read [docs/CONTENT_GUIDE.md](docs/CONTENT_GUIDE.md) before proposing stories, questions, or activities.
- Read [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) before proposing Android implementation work.
- Coding agents must follow [AGENTS.md](AGENTS.md).

## Privacy Statement

The MVP is planned to require no account, no child name, no exact birthdate, no phone number, no email, no analytics, and no network service. Progress and journal data should stay local on the device.

See [docs/PRIVACY.md](docs/PRIVACY.md) for the engineering privacy specification.

## License

Source code in this repository is licensed under the Apache License 2.0. See [LICENSE](LICENSE).

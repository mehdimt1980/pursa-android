# پرسا | Pursa

Pursa is a free and open-source, offline-first Android app for helping Iranian children practice philosophical thinking through stories, questions, and reflection.

پرسا یک برنامه آزاد، رایگان و آفلاین برای کودکان فارسی‌زبان است که به آن‌ها کمک می‌کند پرسش‌های معنادار بپرسند، دلیل بیاورند، دیدگاه‌های متفاوت را ببینند و با احترام گفت‌وگو کنند.

## Project Status

Pursa is in early implementation. The repository now contains a Persian RTL Android development build with a refreshed warm visual identity, a semantic color system, Welcome, Home, three populated content-world entry points, four offline JSON-authored Truth missions, four offline JSON-authored Justice missions, four offline JSON-authored Friendship missions, local progress, replay, Settings/Data & Privacy, and a private local Reflection Journal. No production app, signed release build, or public download exists yet.

## Continuous Integration

Pushes to `main` and pull requests targeting `main` run Android lint, local unit tests, a debug build, a release assembly check, and instrumentation-test APK assembly through GitHub Actions. Successful runs upload temporary APK artifacts for development review.

The APK artifacts are internal development builds, not stable public releases. Instrumentation tests are compiled in CI; device/emulator execution is run locally when an emulator or device is available.

## Offline Story Content

The development build includes a local story-content engine. Authored Persian story content is stored as JSON assets under `app/src/main/assets/content/fa/`, indexed by a manifest, parsed with `kotlinx.serialization`, validated before use, and rendered through fixed-order story steps. The development build currently includes twelve offline Persian missions: four Truth missions (`truth_broken_vase`, `truth_group_photo`, `truth_strange_news`, `truth_friend_secret`), four Justice missions (`justice_last_cake`, `justice_class_representative`, `justice_playground_rule`, `justice_team_prize`), and four Friendship missions (`friendship_new_friend`, `friendship_difficult_promise`, `friendship_whose_side`, `friendship_game_without_them`).

Mission progress is stored locally with Room so in-progress sessions can resume, selected option IDs can be restored, completed missions can be replayed, and all local progress can be cleared from the Data & Privacy screen. The database stores stable story, step, and option IDs only; it does not store child names, profiles, free-text answers, authored Persian story text, scores, analytics, or cloud-sync identifiers. DataStore is not used yet because Phase 7 introduced no genuine small preference. The app remains account-free, backend-free, cloud-sync-free, and analytics-free.

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

The Android application ID is `org.pursa.app`.

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
│       └── main/java/org/pursa/app/
│           ├── content/
│           ├── designsystem/
│           ├── feature/
│           │   ├── home/
│           │   ├── missions/
│           │   ├── story/
│           │   └── world/
│           ├── navigation/
│           └── ui/
│       └── main/assets/content/
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

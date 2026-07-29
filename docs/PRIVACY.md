# Privacy

This document is an engineering privacy specification for `پرسا | Pursa`. It is not final legal advice and is not a final legal privacy policy.

## MVP Privacy Rules

The MVP must require:

- No account.
- No child name.
- No exact birthdate.
- No phone number.
- No email.
- No advertisements.
- No behavioral tracking.
- No third-party analytics.
- No audio upload.
- No backend.
- No cloud synchronization.

## Local Data

Mission progress is stored locally in the app installation using Room. The database stores stable story IDs, current step index, selected step/option IDs, completion status, content revision, session schema version, and local technical timestamps.

The app does not store child name, username, email, phone number, exact birthdate, age, gender, location, contacts, photos, audio, free-text philosophical answers, analytics identifiers, scores, grades, psychological conclusions, or authored Persian story text in progress tables.

There is no account, server upload, cloud sync, behavioral analytics, backend, network transmission, or remote content source for progress data.

The Settings/Data & Privacy screen can clear all locally stored mission progress, active sessions, and selected option IDs. Clearing local progress cannot be undone and does not delete packaged JSON story assets.

Android backup is disabled with `android:allowBackup="false"`, and both legacy backup rules and modern data-extraction rules exclude app data from backup and device-to-device transfer. Uninstalling the app removes the local database as part of normal Android app data removal.

## Data Minimization

Collect only what is necessary for the local app experience. Prefer anonymous local state such as completed story IDs or saved reflections. Do not collect personal identifiers.

## Child Safety

The app must avoid:

- Requests for sensitive personal disclosure.
- Direct communication between children.
- Social networking features.
- Psychological profiling.
- Dark patterns.
- Online AI or open-ended chat in the MVP.

## Future Review

Any future feature that changes data collection, storage, sharing, networking, analytics, backup behavior, audio handling, AI behavior, or account identity must undergo privacy review before implementation.

## Future User Pilots

The production app does not collect research data. The future pilot protocol in `docs/USER_PILOT_PROTOCOL.md` is separate repository documentation, not runtime behavior. Any pilot must use separate consent, data-minimization, retention, safeguarding, and governance review before collecting notes. Do not add participant identifiers, surveys, recordings, analytics, or upload behavior to the app without a new privacy review.

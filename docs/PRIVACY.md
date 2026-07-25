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

Progress should be stored locally on the device. Journal and reflection entries should also be local-only.

Uninstalling the app should remove local data unless Android backup behavior is explicitly configured and reviewed later.

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

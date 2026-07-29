# Security Policy

Pursa is an early-stage offline Android project. It has no account system, backend, analytics, advertising SDK, cloud sync, or Internet permission.

## Reporting a Vulnerability

Please open a private security advisory or contact the maintainers through the repository's security reporting channel when available. Avoid posting exploit details, secrets, private device data, or child data in public issues.

## Release Security

Report these as security-sensitive:

- suspected signing-key compromise;
- checksum mismatch for a published release asset;
- unsigned or debug-signed artifact labeled as official;
- unexpected files attached to a GitHub Release;
- release tag movement after publication;
- committed keystore, signing password, or base64 keystore material.

Maintainers should follow [docs/RELEASE_SECURITY.md](docs/RELEASE_SECURITY.md) for release-key backup, compromise response, and protected release workflow expectations.

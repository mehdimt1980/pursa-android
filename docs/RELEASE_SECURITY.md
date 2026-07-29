# Release Security

Production signing material must never be committed. Official release signing uses GitHub environment secrets and a temporary runner keystore that is deleted in a cleanup step.

## Failure Mode

Official release mode fails closed when any signing input is missing. It must not fall back to debug signing, unsigned release output, or partial publication.

## AAB Verification And Self-Signed Certificates

Android application signing certificates are commonly project-owned and self-signed. That is expected for Pursa's release key and is different from browser/server TLS trust. The release workflow calls `tools/release/verify_signed_artifacts.sh`, which uses `jarsigner -verify -verbose -certs` for AAB signature verification and does not use `jarsigner -strict`, because strict mode fails on the expected untrusted public-CA chain.

This does not disable AAB verification. The workflow prints the full `jarsigner` output, requires `jar verified`, accepts `jarsigner` and `keytool` exit code `4` only for the expected self-signed or PKIX trust-chain warning, rejects missing or zero-length AAB files, and compares the AAB signing-certificate SHA-256 fingerprint against the APK signing certificate reported by `apksigner`.

## Protected Environment

Use a `production-release` GitHub Environment when repository capabilities allow. Recommended protections:

- required reviewer approval;
- access limited to protected branches and tags;
- no access from pull requests or untrusted forks;
- secrets scoped to the environment.

## Compromise Response

Treat signing-key compromise as a critical incident. Preserve evidence, stop releases, rotate GitHub secrets, document affected versions, and decide whether a new application signing identity or store-specific recovery path is required.

Checksum mismatches, changed release tags, unexpected release assets, or unsigned artifacts labeled official should be reported as security issues.

## Reporting

Report suspected vulnerabilities without including unnecessary sensitive exploit details, secrets, child data, or private device data. The app does not collect child conversation data, accounts, analytics, or backend records.

## Future Hardening

GitHub artifact attestations and Play App Signing are future decisions. They are not required for Phase 14 and should be added only with clear permission and verification rules.

# Release Checklist

- `version.properties` has the intended `VERSION_NAME` and increasing positive `VERSION_CODE`.
- Git tag is `v<VERSION_NAME>`.
- `CHANGELOG.md` contains the version.
- `docs/releases/<version>.md` exists and contains privacy, license, checksum, and known-limitation sections.
- Ordinary CI passes without release secrets.
- Release dry run passes with maintainer signing secrets.
- Signed APK and AAB are verified before staging.
- Staging directory contains only allowlisted deterministic asset names.
- SHA-256 checksums are generated after staging.
- SBOM, license report, build info, and release notes are included.
- Draft GitHub Release is reviewed before publication.
- No keystore, password, base64 keystore, APK, AAB, build output, local properties, or private environment dump is staged in Git.
- Release text does not claim formal educational certification, Google Play availability, or external validation that has not happened.

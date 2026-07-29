# Releasing Pursa

Pursa has two separate pipelines:

- Ordinary CI verifies pull requests and branch pushes without release secrets.
- Android Release signs and stages official artifacts only through a matching version tag or maintainer dry run.

## Versioning

`version.properties` is the authoritative source:

```properties
VERSION_CODE=1
VERSION_NAME=0.1.0
```

`VERSION_NAME` uses `MAJOR.MINOR.PATCH` without a leading `v`. Git tags use `vMAJOR.MINOR.PATCH`. `VERSION_CODE` is a positive integer and must increase for official releases.

## Maintainer Release Steps

1. Update `version.properties`.
2. Update `CHANGELOG.md`.
3. Add or update `docs/releases/<version>.md`.
4. Run `python3 tools/release/validate_release.py --version <version>`.
5. Merge the version commit to the protected default branch.
6. Create an annotated tag on that commit, for example `git tag -a v0.1.0 -m "Pursa 0.1.0"`.
7. Push the tag.
8. Let `.github/workflows/android-release.yml` create a draft GitHub Release.
9. Review assets, checksums, SBOM, license report, and build info before publishing the draft.

Do not force-move or recreate published release tags except as part of documented emergency recovery.

## Artifact Names

Official release assets are staged with deterministic names:

- `pursa-<version>-release.apk`
- `pursa-<version>-release.aab`
- `pursa-<version>-checksums-sha256.txt`
- `pursa-<version>-sbom.cdx.json`
- `pursa-<version>-licenses.txt`
- `pursa-<version>-build-info.txt`
- `pursa-<version>-release-notes.md`

Do not publish debug APKs, unsigned APKs, AndroidTest APKs, Gradle caches, local properties, keystores, signing passwords, or private environment dumps.

## Dry Run

Use manual `workflow_dispatch` with `publish=false` to run the same signing/staging path and upload short-lived private workflow artifacts without creating a GitHub Release.

## Keystore Creation

Generate the production keystore outside the repository:

```bash
keytool -genkeypair -v -keystore pursa-release.jks -alias pursa -keyalg RSA -keysize 4096 -validity 10000
```

PowerShell users can run the same `keytool` command from a secure directory outside the repository.

Keep offline backups in at least two secure locations. Store passwords in a password manager. Do not email the keystore, paste it into chat, commit it, or keep the only copy in GitHub Secrets. Losing the key can prevent trusted updates to existing installations.

## GitHub Secrets

Use a protected GitHub Environment named `production-release` when available. Required secrets:

- `PURSA_RELEASE_KEYSTORE_BASE64`
- `PURSA_RELEASE_KEYSTORE_PASSWORD`
- `PURSA_RELEASE_KEY_ALIAS`
- `PURSA_RELEASE_KEY_PASSWORD`

PowerShell base64 encoding outside the repository:

```powershell
[Convert]::ToBase64String(
  [IO.File]::ReadAllBytes("C:\secure\pursa-release.jks")
) | Set-Content -NoNewline "C:\secure\pursa-release.jks.base64.txt"
```

Unix-like encoding:

```bash
base64 < /secure/pursa-release.jks | tr -d '\n' > /secure/pursa-release.jks.base64.txt
```

Copy the single-line value into the GitHub secret. Do not create the base64 file inside the repository.

## Verification

Before tagging, run:

```bash
python3 tools/release/validate_release.py --version 0.1.0
./gradlew :app:lintDebug :app:testDebugUnitTest :app:assembleDebug --stacktrace
./gradlew :app:assembleRelease --stacktrace
./gradlew :app:assembleDebugAndroidTest --stacktrace
git diff --check
```

Do not attempt an official signed build without maintainer signing inputs.

## AAB Signature Verification

The release workflow delegates signed artifact verification to `tools/release/verify_signed_artifacts.sh`. The script verifies the signed APK with Android `apksigner verify --verbose --print-certs`.

The signed AAB is verified with `jarsigner -verify -verbose -certs` and the full output is printed. The verification script intentionally does not use `jarsigner -strict` because Pursa's Android release certificate is a project-owned self-signed application signing certificate, not a certificate rooted in a public web PKI certificate authority. `jarsigner` may still return exit code `4` for that expected trust-chain warning, so the script accepts that code only when the output confirms `jar verified` and reports the expected self-signed or PKIX-path warning.

The workflow still fails for a missing, empty, unsigned, corrupted, or invalidly signed AAB. It also extracts the APK signer SHA-256 certificate digest and compares it with the AAB signer certificate fingerprint so the staged APK and AAB must be signed by the same release certificate. The `keytool -printcert -jarfile` fingerprint extraction may also return exit code `4` for the same expected self-signed trust-chain warning; that is tolerated only when the warning is present and a SHA-256 fingerprint is extracted.

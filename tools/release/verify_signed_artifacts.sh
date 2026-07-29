#!/usr/bin/env bash
set -euo pipefail

APK_PATH="${1:?APK path required}"
AAB_PATH="${2:?AAB path required}"
OUTPUT_DIR="${3:-build/release-verification}"

test -s "${APK_PATH}"
test -s "${AAB_PATH}"
mkdir -p "${OUTPUT_DIR}"

APKSIGNER="${ANDROID_HOME}/build-tools/$(ls "${ANDROID_HOME}/build-tools" | sort -V | tail -n1)/apksigner"
"${APKSIGNER}" verify --verbose --print-certs "${APK_PATH}" | tee "${OUTPUT_DIR}/apksigner-apk.txt"

APK_CERT_SHA256="$(sed -n 's/^.*certificate SHA-256 digest: //p' "${OUTPUT_DIR}/apksigner-apk.txt" | head -n1 | tr -d ':[:space:]' | tr '[:upper:]' '[:lower:]')"
test -n "${APK_CERT_SHA256}"

set +e
jarsigner -verify -verbose -certs "${AAB_PATH}" > "${OUTPUT_DIR}/jarsigner-aab.txt" 2>&1
JARSIGNER_STATUS="$?"
set -e
cat "${OUTPUT_DIR}/jarsigner-aab.txt"

grep -Eq 'jar verified[,.]' "${OUTPUT_DIR}/jarsigner-aab.txt"
if [[ "${JARSIGNER_STATUS}" -ne 0 ]]; then
    test "${JARSIGNER_STATUS}" -eq 4
    grep -Eq 'self-signed|PKIX path building failed|unable to find valid certification path' "${OUTPUT_DIR}/jarsigner-aab.txt"
fi

set +e
keytool -printcert -jarfile "${AAB_PATH}" > "${OUTPUT_DIR}/keytool-aab-cert.txt" 2>&1
KEYTOOL_STATUS="$?"
set -e
cat "${OUTPUT_DIR}/keytool-aab-cert.txt"

AAB_CERT_SHA256="$(sed -n 's/.*SHA256: //p' "${OUTPUT_DIR}/keytool-aab-cert.txt" | head -n1 | tr -d ':[:space:]' | tr '[:upper:]' '[:lower:]')"
test -n "${AAB_CERT_SHA256}"

if [[ "${KEYTOOL_STATUS}" -ne 0 ]]; then
    grep -Eq 'self-signed|PKIX path building failed|unable to find valid certification path' "${OUTPUT_DIR}/keytool-aab-cert.txt" || true
fi

test "${APK_CERT_SHA256}" = "${AAB_CERT_SHA256}"
echo "APK and AAB signing certificate SHA-256: ${APK_CERT_SHA256}"

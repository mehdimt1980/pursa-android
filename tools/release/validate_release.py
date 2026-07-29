#!/usr/bin/env python3
"""Validate Pursa release version, notes, and optional staged assets."""

from __future__ import annotations

import argparse
import re
import sys
from pathlib import Path


ROOT = Path(__file__).resolve().parents[2]
SEMVER = re.compile(r"^(0|[1-9][0-9]*)\.(0|[1-9][0-9]*)\.(0|[1-9][0-9]*)$")
PLACEHOLDERS = ("TODO", "TBD", "CHANGE ME")


def read_version() -> dict[str, str]:
    path = ROOT / "version.properties"
    values: dict[str, str] = {}
    for line in path.read_text(encoding="utf-8").splitlines():
        line = line.strip()
        if not line or line.startswith("#"):
            continue
        if "=" not in line:
            raise ValueError(f"Malformed version.properties line: {line}")
        key, value = line.split("=", 1)
        values[key.strip()] = value.strip()
    return values


def expected_assets(version: str) -> set[str]:
    return {
        f"pursa-{version}-release.apk",
        f"pursa-{version}-release.aab",
        f"pursa-{version}-checksums-sha256.txt",
        f"pursa-{version}-sbom.cdx.json",
        f"pursa-{version}-licenses.txt",
        f"pursa-{version}-build-info.txt",
        f"pursa-{version}-release-notes.md",
    }


def require(condition: bool, message: str, errors: list[str]) -> None:
    if not condition:
        errors.append(message)


def validate(args: argparse.Namespace) -> list[str]:
    errors: list[str] = []
    values = read_version()
    version_name = values.get("VERSION_NAME", "")
    version_code = values.get("VERSION_CODE", "")
    expected_tag = f"v{version_name}"

    require(bool(SEMVER.fullmatch(version_name)), f"Invalid VERSION_NAME: {version_name}", errors)
    require(version_code.isdigit() and int(version_code) > 0, f"Invalid VERSION_CODE: {version_code}", errors)

    if args.version:
        require(args.version == version_name, f"Input version {args.version} does not match {version_name}", errors)

    if args.tag:
        require(not any(ch.isspace() for ch in args.tag), "Tag must not contain whitespace", errors)
        require(args.tag == expected_tag, f"Tag {args.tag} does not match {expected_tag}", errors)

    changelog = (ROOT / "CHANGELOG.md").read_text(encoding="utf-8")
    require("## [Unreleased]" in changelog, "CHANGELOG.md must contain an Unreleased section", errors)
    require(f"## [{version_name}]" in changelog, f"CHANGELOG.md must contain {version_name}", errors)

    notes_path = ROOT / "docs" / "releases" / f"{version_name}.md"
    require(notes_path.exists(), f"Missing release notes: {notes_path.relative_to(ROOT)}", errors)
    if notes_path.exists():
        notes = notes_path.read_text(encoding="utf-8")
        require(version_name in notes, "Release notes must include the version", errors)
        require("Known limitations" in notes, "Release notes must include known limitations", errors)
        require("Privacy" in notes, "Release notes must include privacy facts", errors)
        require("Checksums" in notes, "Release notes must reference checksums", errors)
        require("License" in notes, "Release notes must reference license information", errors)
        for marker in PLACEHOLDERS:
            require(marker not in notes, f"Release notes contain placeholder marker: {marker}", errors)

    if args.staging_dir:
        staging = Path(args.staging_dir)
        require(staging.exists(), f"Staging directory does not exist: {staging}", errors)
        if staging.exists():
            actual = {path.name for path in staging.iterdir() if path.is_file()}
            expected = expected_assets(version_name)
            require(actual == expected, f"Staging assets mismatch. Expected {sorted(expected)}, got {sorted(actual)}", errors)
            require(all(" " not in name for name in actual), "Staging asset names must not contain spaces", errors)

    return errors


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("--version")
    parser.add_argument("--tag")
    parser.add_argument("--staging-dir")
    args = parser.parse_args()
    errors = validate(args)
    if errors:
        for error in errors:
            print(f"ERROR: {error}", file=sys.stderr)
        return 1
    values = read_version()
    print(f"VERSION_NAME={values['VERSION_NAME']}")
    print(f"VERSION_CODE={values['VERSION_CODE']}")
    print(f"TAG=v{values['VERSION_NAME']}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())

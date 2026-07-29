#!/usr/bin/env python3
"""Generate SBOM, license report, and build information for Pursa releases."""

from __future__ import annotations

import argparse
import datetime as dt
import json
import re
import subprocess
from pathlib import Path


ROOT = Path(__file__).resolve().parents[2]


def read_version() -> dict[str, str]:
    values: dict[str, str] = {}
    for line in (ROOT / "version.properties").read_text(encoding="utf-8").splitlines():
        line = line.strip()
        if line and not line.startswith("#") and "=" in line:
            key, value = line.split("=", 1)
            values[key.strip()] = value.strip()
    return values


def read_catalog_components() -> list[dict[str, str]]:
    text = (ROOT / "gradle" / "libs.versions.toml").read_text(encoding="utf-8")
    versions: dict[str, str] = {}
    components: list[dict[str, str]] = []
    section = None
    for raw_line in text.splitlines():
        line = raw_line.strip()
        if not line or line.startswith("#"):
            continue
        if line.startswith("[") and line.endswith("]"):
            section = line.strip("[]")
            continue
        if section == "versions" and "=" in line:
            key, value = line.split("=", 1)
            versions[key.strip()] = value.strip().strip('"')
        elif section == "libraries":
            match = re.match(r'([A-Za-z0-9_.-]+)\s*=\s*\{\s*group\s*=\s*"([^"]+)",\s*name\s*=\s*"([^"]+)"(?:,\s*version\.ref\s*=\s*"([^"]+)")?', line)
            if match:
                alias, group, name, version_ref = match.groups()
                components.append(
                    {
                        "alias": alias,
                        "group": group,
                        "name": name,
                        "version": versions.get(version_ref or "", "managed-by-bom"),
                    }
                )
    return components


def git_output(args: list[str]) -> str:
    try:
        return subprocess.check_output(["git", *args], cwd=ROOT, text=True, stderr=subprocess.DEVNULL).strip()
    except Exception:
        return "unknown"


def write_metadata(output_dir: Path) -> None:
    values = read_version()
    version = values["VERSION_NAME"]
    output_dir.mkdir(parents=True, exist_ok=True)
    components = read_catalog_components()

    sbom = {
        "bomFormat": "CycloneDX",
        "specVersion": "1.5",
        "version": 1,
        "metadata": {
            "timestamp": dt.datetime.now(dt.timezone.utc).isoformat(),
            "component": {
                "type": "application",
                "name": "pursa",
                "group": "org.pursa",
                "version": version,
            },
        },
        "components": [
            {
                "type": "library",
                "group": component["group"],
                "name": component["name"],
                "version": component["version"],
                "purl": f"pkg:maven/{component['group']}/{component['name']}@{component['version']}",
            }
            for component in components
        ],
    }
    (output_dir / f"pursa-{version}-sbom.cdx.json").write_text(
        json.dumps(sbom, indent=2, sort_keys=True) + "\n",
        encoding="utf-8",
    )

    license_lines = [
        "Pursa license and attribution report",
        f"Version: {version}",
        "",
        "Project license: Apache License 2.0. See LICENSE.",
        "No external image, audio, analytics, backend, or advertising SDK dependency is bundled.",
        "",
        "Tracked Gradle dependency coordinates:",
    ]
    for component in sorted(components, key=lambda item: (item["group"], item["name"])):
        license_lines.append(f"- {component['group']}:{component['name']}:{component['version']}")
    (output_dir / f"pursa-{version}-licenses.txt").write_text("\n".join(license_lines) + "\n", encoding="utf-8")

    build_info = [
        "Pursa release build information",
        f"Version name: {version}",
        f"Version code: {values['VERSION_CODE']}",
        f"Git commit: {git_output(['rev-parse', 'HEAD'])}",
        f"Git tag: v{version}",
        "Application ID: org.pursa.app",
        "Official assets are signed only when PURSA_OFFICIAL_RELEASE=true and release secrets are complete.",
        "No child data, local progress database, keystore, signing password, or local properties file is included.",
    ]
    (output_dir / f"pursa-{version}-build-info.txt").write_text("\n".join(build_info) + "\n", encoding="utf-8")


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("--output-dir", required=True)
    args = parser.parse_args()
    write_metadata(Path(args.output_dir))
    return 0


if __name__ == "__main__":
    raise SystemExit(main())

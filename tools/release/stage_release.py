#!/usr/bin/env python3
"""Stage signed release artifacts with deterministic names and checksums."""

from __future__ import annotations

import argparse
import hashlib
import shutil
from pathlib import Path


ROOT = Path(__file__).resolve().parents[2]


def read_version() -> str:
    for line in (ROOT / "version.properties").read_text(encoding="utf-8").splitlines():
        if line.startswith("VERSION_NAME="):
            return line.split("=", 1)[1].strip()
    raise ValueError("VERSION_NAME missing")


def sha256(path: Path) -> str:
    digest = hashlib.sha256()
    with path.open("rb") as handle:
        for chunk in iter(lambda: handle.read(1024 * 1024), b""):
            digest.update(chunk)
    return digest.hexdigest()


def stage(apk: Path, aab: Path, metadata_dir: Path, output_dir: Path) -> None:
    version = read_version()
    output_dir.mkdir(parents=True, exist_ok=True)
    required_metadata = [
        metadata_dir / f"pursa-{version}-sbom.cdx.json",
        metadata_dir / f"pursa-{version}-licenses.txt",
        metadata_dir / f"pursa-{version}-build-info.txt",
    ]
    for path in [apk, aab, *required_metadata]:
        if not path.exists():
            raise FileNotFoundError(path)

    assets = {
        apk: output_dir / f"pursa-{version}-release.apk",
        aab: output_dir / f"pursa-{version}-release.aab",
        metadata_dir / f"pursa-{version}-sbom.cdx.json": output_dir / f"pursa-{version}-sbom.cdx.json",
        metadata_dir / f"pursa-{version}-licenses.txt": output_dir / f"pursa-{version}-licenses.txt",
        metadata_dir / f"pursa-{version}-build-info.txt": output_dir / f"pursa-{version}-build-info.txt",
        ROOT / "docs" / "releases" / f"{version}.md": output_dir / f"pursa-{version}-release-notes.md",
    }
    for source, destination in assets.items():
        shutil.copy2(source, destination)

    checksum_lines = []
    for path in sorted(output_dir.iterdir()):
        if path.name.endswith("checksums-sha256.txt"):
            continue
        checksum_lines.append(f"{sha256(path)}  {path.name}")
    (output_dir / f"pursa-{version}-checksums-sha256.txt").write_text(
        "\n".join(checksum_lines) + "\n",
        encoding="utf-8",
    )


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("--apk", required=True)
    parser.add_argument("--aab", required=True)
    parser.add_argument("--metadata-dir", required=True)
    parser.add_argument("--output-dir", required=True)
    args = parser.parse_args()
    stage(Path(args.apk), Path(args.aab), Path(args.metadata_dir), Path(args.output_dir))
    return 0


if __name__ == "__main__":
    raise SystemExit(main())

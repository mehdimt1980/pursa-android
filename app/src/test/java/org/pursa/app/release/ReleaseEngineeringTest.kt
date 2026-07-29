package org.pursa.app.release

import java.nio.file.Files
import java.nio.file.Path
import java.util.Properties
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ReleaseEngineeringTest {
    @Test
    fun versionPropertiesDefineSemanticPositiveAndroidVersion() {
        val version = versionProperties()
        val versionName = version.getProperty("VERSION_NAME")
        val versionCode = version.getProperty("VERSION_CODE")

        assertTrue(versionName.matches(Regex("""(0|[1-9][0-9]*)\.(0|[1-9][0-9]*)\.(0|[1-9][0-9]*)""")))
        assertTrue(versionCode.toInt() > 0)

        val buildFile = readProjectFile("app/build.gradle.kts")
        assertTrue(buildFile.contains("rootProject.file(\"version.properties\")"))
        assertFalse(buildFile.contains("versionName = \""))
        assertFalse(buildFile.contains("versionCode = 1"))
    }

    @Test
    fun releaseSigningFailsClosedWithoutDebugFallback() {
        val buildFile = readProjectFile("app/build.gradle.kts")

        assertTrue(buildFile.contains("PURSA_OFFICIAL_RELEASE"))
        assertTrue(buildFile.contains("PURSA_RELEASE_KEYSTORE_PATH"))
        assertTrue(buildFile.contains("PURSA_RELEASE_KEYSTORE_PASSWORD"))
        assertTrue(buildFile.contains("PURSA_RELEASE_KEY_ALIAS"))
        assertTrue(buildFile.contains("PURSA_RELEASE_KEY_PASSWORD"))
        assertTrue(buildFile.contains("officialRelease && !releaseSigningComplete"))
        assertFalse(buildFile.contains("signingConfigs.getByName(\"debug\")"))
    }

    @Test
    fun releaseWorkflowUsesDeliberateTriggersAndLeastPrivilege() {
        val workflow = readProjectFile(".github/workflows/android-release.yml")

        assertTrue(workflow.contains("tags:"))
        assertTrue(workflow.contains("workflow_dispatch:"))
        assertTrue(workflow.contains("environment: production-release"))
        assertTrue(workflow.contains("contents: read"))
        assertTrue(workflow.contains("contents: write"))
        assertTrue(workflow.contains("cancel-in-progress: false"))
        assertTrue(workflow.contains("python3 tools/release/validate_release.py"))
        assertTrue(workflow.contains("PURSA_OFFICIAL_RELEASE: \"true\""))
        assertTrue(workflow.contains("apksigner"))
        assertTrue(workflow.contains("jarsigner -verify -verbose -certs"))
        assertFalse(workflow.contains("jarsigner -verify -strict"))
        assertTrue(workflow.contains("cat build/release-verification/jarsigner-aab.txt"))
        assertTrue(workflow.contains("grep -Eq 'jar verified[,.]'"))
        assertTrue(workflow.contains("keytool -printcert -jarfile"))
        assertTrue(workflow.contains("APK_CERT_SHA256"))
        assertTrue(workflow.contains("AAB_CERT_SHA256"))
        assertTrue(workflow.contains("test \"${'$'}{APK_CERT_SHA256}\" = \"${'$'}{AAB_CERT_SHA256}\""))
        assertTrue(workflow.contains("gh release create"))
        assertTrue(workflow.contains("--draft"))
        assertFalse(workflow.contains("pull_request:"))
        assertFalse(workflow.contains("write-all"))
        assertFalse(workflow.contains("packages: write"))
        assertFalse(workflow.contains("id-token: write"))
    }

    @Test
    fun releaseDocumentationAndScriptsExist() {
        listOf(
            "CHANGELOG.md",
            "SECURITY.md",
            "docs/RELEASING.md",
            "docs/RELEASE_SECURITY.md",
            "docs/RELEASE_CHECKLIST.md",
            "docs/releases/0.1.0.md",
            "tools/release/validate_release.py",
            "tools/release/generate_release_metadata.py",
            "tools/release/stage_release.py",
            "signing.properties.example",
        ).forEach { path ->
            assertTrue("$path must exist", Files.exists(projectPath(path)))
        }
    }

    @Test
    fun artifactNamesAreDeterministicAndAllowlisted() {
        val script = readProjectFile("tools/release/validate_release.py")
        val version = versionProperties().getProperty("VERSION_NAME")
        val expected = listOf(
            "release.apk",
            "release.aab",
            "checksums-sha256.txt",
            "sbom.cdx.json",
            "licenses.txt",
            "build-info.txt",
            "release-notes.md",
        )

        expected.forEach { suffix ->
            assertTrue(script.contains("pursa-{version}-$suffix"))
        }
        assertTrue(script.contains("actual == expected"))
        assertEquals("0.1.0", version)
    }

    @Test
    fun releaseEngineeringDoesNotAddRuntimePermissionsOrTrackedSigningMaterial() {
        val manifest = readProjectFile("app/src/main/AndroidManifest.xml")
        val gitignore = readProjectFile(".gitignore")
        val releasing = readProjectFile("docs/RELEASING.md")

        assertFalse(manifest.contains("android.permission.INTERNET"))
        assertTrue(gitignore.contains("*.jks"))
        assertTrue(gitignore.contains("*.keystore"))
        assertTrue(gitignore.contains("signing.properties"))
        assertTrue(releasing.contains("Do not create the base64 file inside the repository."))
    }

    private fun versionProperties(): Properties =
        Properties().apply {
            projectPath("version.properties").toFile().inputStream().use(::load)
        }

    private fun readProjectFile(relativePath: String): String =
        String(Files.readAllBytes(projectPath(relativePath)), Charsets.UTF_8)

    private fun projectPath(relativePath: String): Path {
        val userDir = Path.of(System.getProperty("user.dir"))
        val candidates = listOf(
            userDir.resolve(relativePath),
            userDir.parent?.resolve(relativePath),
        ).filterNotNull()
        return candidates.firstOrNull { Files.exists(it) } ?: candidates.first()
    }
}

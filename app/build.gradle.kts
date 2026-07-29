import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

val versionProperties = Properties().apply {
    rootProject.file("version.properties").inputStream().use(::load)
}

fun requiredVersionProperty(name: String): String =
    versionProperties.getProperty(name)?.trim()?.takeIf { it.isNotEmpty() }
        ?: throw GradleException("Missing $name in version.properties")

val pursaVersionCode = requiredVersionProperty("VERSION_CODE").toIntOrNull()
    ?: throw GradleException("VERSION_CODE must be an integer")
val pursaVersionName = requiredVersionProperty("VERSION_NAME")

fun releaseValue(name: String): String? =
    providers.gradleProperty(name).orNull?.trim()?.takeIf { it.isNotEmpty() }
        ?: providers.environmentVariable(name).orNull?.trim()?.takeIf { it.isNotEmpty() }

val officialRelease = releaseValue("PURSA_OFFICIAL_RELEASE")?.equals("true", ignoreCase = true) == true
val releaseKeystorePath = releaseValue("PURSA_RELEASE_KEYSTORE_PATH")
val releaseKeystorePassword = releaseValue("PURSA_RELEASE_KEYSTORE_PASSWORD")
val releaseKeyAlias = releaseValue("PURSA_RELEASE_KEY_ALIAS")
val releaseKeyPassword = releaseValue("PURSA_RELEASE_KEY_PASSWORD")
val releaseSigningComplete = listOf(
    releaseKeystorePath,
    releaseKeystorePassword,
    releaseKeyAlias,
    releaseKeyPassword,
).all { it != null }

if (officialRelease && !releaseSigningComplete) {
    throw GradleException(
        "PURSA_OFFICIAL_RELEASE=true requires PURSA_RELEASE_KEYSTORE_PATH, " +
            "PURSA_RELEASE_KEYSTORE_PASSWORD, PURSA_RELEASE_KEY_ALIAS, and PURSA_RELEASE_KEY_PASSWORD.",
    )
}

android {
    namespace = "org.pursa.app"
    compileSdk = 36

    defaultConfig {
        applicationId = "org.pursa.app"
        minSdk = 26
        targetSdk = 36
        versionCode = pursaVersionCode
        versionName = pursaVersionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        if (releaseSigningComplete) {
            create("release") {
                storeFile = file(releaseKeystorePath!!)
                storePassword = releaseKeystorePassword
                keyAlias = releaseKeyAlias
                keyPassword = releaseKeyPassword
            }
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
        }
        release {
            isMinifyEnabled = false
            if (releaseSigningComplete) {
                signingConfig = signingConfigs.getByName("release")
            }
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    buildFeatures {
        compose = true
    }
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
    arg("room.incremental", "true")
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    implementation(libs.kotlinx.serialization.json)
    ksp(libs.androidx.room.compiler)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.room.testing)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.runner)

    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}

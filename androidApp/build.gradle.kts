/**
 * Configures the installable Android app and its shared UI dependency.
 *
 * The Android block defines application identity, supported SDK levels, and
 * JVM compatibility. Activity Compose connects the Android activity to App()
 * from the shared module.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.stefan.neonarena"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.stefan.neonarena"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "0.1.0"
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(libs.androidx.activity.compose)
}

import org.jetbrains.kotlin.gradle.dsl.JvmTarget

/**
 * Configures the Kotlin code shared by Android, desktop, and iOS.
 *
 * commonMain contains the shared screen. Platform targets compile that code for Android, the JVM, and Apple
 * ARM64. The iOS targets export a static NeonShared framework for Swift.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    android {
        namespace = "com.stefan.neonarena.shared"
        compileSdk = 36
        minSdk = 26
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(iosArm64(), iosSimulatorArm64()).forEach { target ->
        target.binaries.framework {
            baseName = "NeonShared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.ui)
        }
    }
}

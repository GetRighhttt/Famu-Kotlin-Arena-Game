import org.jetbrains.kotlin.gradle.dsl.JvmTarget

/**
 * Configures the desktop JVM launcher and Compose desktop runtime.
 *
 * The shared module supplies the screen. Compose desktop supplies windowing
 * and the runtime for this operating system. mainClass points to the generated
 * JVM class containing the top-level main function in Main.kt.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    sourceSets {
        jvmMain.dependencies {
            implementation(project(":shared"))
            implementation(compose.desktop.currentOs)
        }
        jvmTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.stefan.neonarena.MainKt"
    }
}

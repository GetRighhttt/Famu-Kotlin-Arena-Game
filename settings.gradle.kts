/**
 * Names the project, includes its Gradle modules, and defines repositories.
 *
 * Plugin repositories resolve build plugins. Dependency repositories resolve
 * libraries used by shared, androidApp, and desktopApp. Xcode owns the iOS
 * launcher project, so iosApp is not included as a Gradle module.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "NeonArena"
include(":shared", ":androidApp", ":desktopApp")

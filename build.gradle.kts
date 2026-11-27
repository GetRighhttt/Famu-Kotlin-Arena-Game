/**
 * Declares plugin versions for the whole project without applying them here.
 *
 * Each module applies the plugins it needs. The aliases come from
 * gradle/libs.versions.toml, so toolchain versions have one source.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.multiplatform.library) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.compose.compiler) apply false
}

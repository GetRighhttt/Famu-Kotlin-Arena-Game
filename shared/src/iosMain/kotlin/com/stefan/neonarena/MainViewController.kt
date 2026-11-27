package com.stefan.neonarena

import androidx.compose.ui.window.ComposeUIViewController

/**
 * Exposes the shared [App] as a UIKit view controller for the SwiftUI launcher.
 *
 * ComposeUIViewController hosts Compose content inside iOS. Kotlin exports
 * this top-level function through the NeonShared framework.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
fun MainViewController() = ComposeUIViewController {
    App()
}

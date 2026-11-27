package com.stefan.neonarena

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

/**
 * Starts the desktop app with a resizable window containing the shared [App].
 *
 * The initial window is 960 by 640 density-independent pixels. Closing it
 * exits the Compose application rather than leaving a background process.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Neon Arena",
        state = rememberWindowState(width = 960.dp, height = 640.dp),
    ) {
        App()
    }
}

package com.stefan.neonarena

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Neon Arena",
        state = rememberWindowState(width = 960.dp, height = 640.dp),
    ) {
        App()
    }
}

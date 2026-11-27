package com.stefan.neonarena

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

/**
 * Starts the Android app and displays the shared Compose screen.
 *
 * Android calls [onCreate] when it creates the activity. [setContent] hosts
 * [App], and edge-to-edge drawing lets the arena handle safe drawing insets.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App()
        }
    }
}

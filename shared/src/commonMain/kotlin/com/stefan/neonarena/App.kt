package com.stefan.neonarena

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

/**
 * Displays the shared starter screen called by each platform launcher.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
@Composable
fun App() {
    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFF090D19)),
        contentAlignment = Alignment.Center,
    ) {
        BasicText(
            text = "Neon Arena",
            style = TextStyle(color = Color(0xFF66FFE3), fontSize = 32.sp),
        )
    }
}

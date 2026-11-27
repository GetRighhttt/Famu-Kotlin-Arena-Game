package com.stefan.neonarena

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Draws a player circle at the center of the arena using local pixel coordinates.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
@Composable
fun App() {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090D19))
            .safeDrawingPadding()
            .padding(24.dp)
            .border(1.dp, Color(0xFF27665F))
            .clipToBounds(),
    ) {
        drawRect(color = Color(0xFF101B2A))
        val playerPosition = Offset(size.width / 2f, size.height / 2f)
        val playerRadius = 18.dp.toPx()
        drawCircle(
            color = Color(0xFF66FFE3),
            radius = playerRadius,
            center = playerPosition,
        )
    }
}

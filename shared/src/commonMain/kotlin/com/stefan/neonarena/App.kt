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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

@Composable
fun App() {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090D19))
            .safeDrawingPadding()
            .padding(24.dp)
            .border(1.dp, Color(0xFF27665F))
            .clipToBounds()
            .semantics {
                contentDescription = "Arena with a cyan player at the center"
            },
    ) {
        drawRect(color = Color(0xFF101B2A))

        val playerPosition = Offset(
            x = size.width / 20f,
            y = size.height / 2f,
        )
        val playerRadius = 18.dp.toPx()

        drawCircle(
            color = Color(0xFF66FFE3),
            radius = playerRadius,
            center = playerPosition,
        )
    }
}

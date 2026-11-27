package com.stefan.neonarena.game.rendering

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import com.stefan.neonarena.ui.theme.GameColors

/**
 * Draws the field surface and evenly spaced horizontal yard lines.
 *
 * Line positions use fractions of the canvas height so the field resizes
 * with the arena. Draw the player afterward so it appears above the markings.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
fun DrawScope.drawFootballField() {
    drawRect(color = GameColors.arenaSurface)

    val sections = 10
    val sectionHeight = size.height / sections
    for (line in 1 until sections) {
        val y = sectionHeight * line
        drawLine(
            color = GameColors.fieldLine,
            start = Offset(x = 0f, y = y),
            end = Offset(x = size.width, y = y),
            strokeWidth = 1.dp.toPx(),
        )
    }
}

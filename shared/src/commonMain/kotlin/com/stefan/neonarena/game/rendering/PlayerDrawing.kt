package com.stefan.neonarena.game.rendering

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.stefan.neonarena.game.model.Player

/**
 * Draws a player using the radius that fits the current canvas.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
fun DrawScope.drawPlayer(player: Player, color: Color = Color(0xFF66FFE3)) {
    drawCircle(
        color = color,
        radius = player.radiusIn(size),
        center = player.position,
    )
}

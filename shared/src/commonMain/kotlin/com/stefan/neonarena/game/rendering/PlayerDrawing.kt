package com.stefan.neonarena.game.rendering

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.stefan.neonarena.game.model.Player
import com.stefan.neonarena.ui.theme.GameColors

/**
 * Draws a FAMU-orange player using the radius that fits the current canvas.
 * Callers can override its color without changing the player data.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
fun DrawScope.drawPlayer(player: Player, color: Color = GameColors.famuOrange) {
    drawCircle(
        color = color,
        radius = player.radiusIn(size),
        center = player.position,
    )
}

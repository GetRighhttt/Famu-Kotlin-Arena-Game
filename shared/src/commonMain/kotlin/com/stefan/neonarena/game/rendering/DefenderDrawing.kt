package com.stefan.neonarena.game.rendering

import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.stefan.neonarena.game.model.Player
import com.stefan.neonarena.ui.theme.GameColors

/**
 * Draws the BCU defender in maroon with a gold outline inside its visible radius.
 * Reuses player drawing for the body so both teams share geometry rendering.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
fun DrawScope.drawDefender(defender: Player) {
    drawPlayer(defender, color = GameColors.bcuMaroon)
    val radius = defender.radiusIn(size)
    val outlineWidth = minOf(2.dp.toPx(), radius)
    if (outlineWidth <= 0f) return
    drawCircle(
        color = GameColors.bcuGold,
        center = defender.position,
        radius = radius - outlineWidth / 2f,
        style = Stroke(width = outlineWidth),
    )
}

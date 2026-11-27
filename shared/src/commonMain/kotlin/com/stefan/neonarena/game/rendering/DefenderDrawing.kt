package com.stefan.neonarena.game.rendering

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.stefan.neonarena.game.model.Player
import com.stefan.neonarena.ui.theme.GameColors
import kotlin.math.PI
import kotlin.math.atan2

/**
 * Turns the maroon-and-gold defender toward the runner. The uniform faces up
 * at zero degrees, so a target directly below it needs a half turn.
 * atan2 normally measures from the positive x axis. Passing (dx, -dy) measures
 * clockwise from up, which matches the uniform. Multiplying radians by 180 / PI
 * gives the degrees that DrawScope.rotate expects.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
fun DrawScope.drawDefender(defender: Player, target: Offset? = null) {
    val direction = target?.minus(defender.position)
    val heading = if (direction == null || direction == Offset.Zero) 180f else
        (atan2(direction.x, -direction.y) * 180f / PI).toFloat()
    drawFootballPlayer(defender, jersey = GameColors.bcuMaroon, helmet = GameColors.bcuGold, heading = heading, carriesBall = false)
}

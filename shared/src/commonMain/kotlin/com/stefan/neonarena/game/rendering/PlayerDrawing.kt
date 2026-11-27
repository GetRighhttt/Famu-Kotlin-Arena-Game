package com.stefan.neonarena.game.rendering

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import com.stefan.neonarena.game.model.Player
import com.stefan.neonarena.ui.theme.GameColors

/**
 * Draws the FAMU ball carrier facing the far end zone.
 * Both uniforms use an 18-unit drawing radius scaled to the player's visible radius.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
fun DrawScope.drawPlayer(player: Player, color: Color = GameColors.famuOrange) {
    drawFootballPlayer(player, jersey = color, helmet = GameColors.famuGreen, heading = 0f, carriesBall = true)
}

internal fun DrawScope.drawFootballPlayer(player: Player, jersey: Color, helmet: Color, heading: Float, carriesBall: Boolean) {
    val scale = player.radiusIn(size) / 18f
    if (scale <= 0f) return
    val gear = GameColors.background
    withTransform({
        translate(player.position.x, player.position.y)
        rotate(heading, Offset.Zero)
        scale(scale, scale, Offset.Zero)
    }) {
        // The feet and gloves sit underneath the jersey and shoulder pads.
        for (side in -1..1 step 2) {
            val x = side.toFloat()
            val footY = if (side < 0) 11f else 14f
            drawLine(gear, Offset(x * 5f, 5f), Offset(x * 6f, footY), 4.5f, StrokeCap.Round)
            drawLine(Color.White, Offset(x * 4f, 4f), Offset(x * 6f, footY - 3f), 5f, StrokeCap.Round)
            drawLine(jersey, Offset(x * 10f, -3f), Offset(x * 13f, 3f), 6f, StrokeCap.Round)
            drawCircle(Color.White, radius = 2.6f, center = Offset(x * 13f, 5f))
        }
        drawOval(jersey, Offset(-8f, -6f), Size(16f, 17f))
        drawOval(jersey, Offset(-14f, -7f), Size(28f, 11f))
        drawLine(helmet, Offset(-11f, -4f), Offset(-10f, 0f), 2f)
        drawLine(helmet, Offset(11f, -4f), Offset(10f, 0f), 2f)
        if (carriesBall) {
            drawOval(GameColors.footballLeather, Offset(7.5f, -1f), Size(7f, 12f))
            drawLine(Color.White, Offset(11f, 2f), Offset(11f, 5f), 1.2f)
        }

        // The facemask projects past the front of the helmet.
        drawOval(gear, Offset(-6f, -16f), Size(12f, 10f), style = Stroke(width = 1.6f))
        drawOval(helmet, Offset(-7.5f, -13f), Size(15f, 15f))
        drawLine(Color.White, Offset(0f, -12.5f), Offset(0f, 1f), 2f)
    }
}

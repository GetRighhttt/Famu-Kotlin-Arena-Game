package com.stefan.neonarena.ui.arena

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stefan.neonarena.game.model.Player
import com.stefan.neonarena.game.state.FieldCamera
import com.stefan.neonarena.ui.theme.GameColors
import kotlin.math.ceil

/**
 * Keeps the matchup and distance to the goal in screen coordinates while the field scrolls.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
@Composable
internal fun MatchupStrip() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Canvas(Modifier.size(26.dp, 21.dp)) {
            for (stripe in 0..2) {
                val x = size.width * (0.07f + stripe * 0.26f)
                drawLine(
                    if (stripe == 0) GameColors.cream else GameColors.famuOrange,
                    Offset(x, size.height * 0.85f), Offset(x + size.width * 0.26f, size.height * 0.15f),
                    strokeWidth = size.width * 0.13f,
                )
            }
        }
        BasicText(
            "Florida Classic", modifier = Modifier.weight(1f),
            style = TextStyle(color = GameColors.cream, fontWeight = FontWeight.Black, fontStyle = FontStyle.Italic, fontSize = 21.sp),
        )
        TeamLabel("FAMU", GameColors.famuOrange)
        TeamLabel("BCU", GameColors.bcuGold)
    }
}

@Composable
private fun TeamLabel(name: String, accent: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        Box(Modifier.size(3.dp, 13.dp).background(accent))
        BasicText(name, style = TextStyle(color = GameColors.cream, fontWeight = FontWeight.Bold, fontSize = 13.sp))
    }
}

@Composable
internal fun DriveStrip(player: Player?, camera: FieldCamera) {
    val playingHeight = camera.fieldSize.height - 2f * camera.endZoneHeight
    val distance = if (player != null && playingHeight > 0f) {
        val leadingEdge = player.position.y - player.radiusIn(camera.viewport)
        ((leadingEdge - camera.endZoneHeight) / playingHeight).coerceIn(0f, 1f)
    } else 1f
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        BasicText("TO GOAL", style = TextStyle(
            color = GameColors.fieldLine, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp,
        ))
        Canvas(Modifier.weight(1f).height(9.dp)) {
            val y = size.height / 2f
            val marker = Offset(size.width * (1f - distance), y)
            drawLine(GameColors.rail, Offset(0f, y), Offset(size.width, y), 2.dp.toPx())
            drawLine(GameColors.famuOrange, Offset(0f, y), marker, 2.dp.toPx())
            for (tick in 1..9) {
                val x = size.width * tick / 10f
                drawLine(GameColors.fieldLine, Offset(x, y - 2.dp.toPx()), Offset(x, y + 2.dp.toPx()), 1.dp.toPx())
            }
            drawCircle(GameColors.famuOrange, 3.dp.toPx(), marker)
        }
        BasicText("${ceil(distance * 100).toInt()} YD", style = TextStyle(
            color = GameColors.cream, fontWeight = FontWeight.Bold, fontSize = 14.sp,
        ))
    }
}

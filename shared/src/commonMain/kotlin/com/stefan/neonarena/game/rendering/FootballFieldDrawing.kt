package com.stefan.neonarena.game.rendering

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import com.stefan.neonarena.ui.theme.GameColors

/**
 * Draws the world-sized field, numbered yard lines, one-yard hash marks, end-zone lettering, and goalposts.
 *
 * Coordinates belong to the full field, not the visible canvas. The caller
 * applies camera translation and clipping before composing players above it.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
fun DrawScope.drawFootballField(fieldSize: Size, endZoneHeight: Float, textMeasurer: TextMeasurer) {
    drawRect(color = GameColors.arenaSurface, size = fieldSize)
    drawRect(color = GameColors.famuGreen, size = Size(fieldSize.width, endZoneHeight))
    drawRect(
        color = GameColors.famuGreen,
        topLeft = Offset(0f, fieldSize.height - endZoneHeight),
        size = Size(fieldSize.width, endZoneHeight),
    )
    val sectionHeight = (fieldSize.height - 2f * endZoneHeight) / 10f
    for (line in 0..10) {
        val y = endZoneHeight + sectionHeight * line
        drawLine(
            color = if (line == 0 || line == 10) GameColors.famuOrange else GameColors.fieldLine,
            start = Offset(0f, y), end = Offset(fieldSize.width, y), strokeWidth = 2.dp.toPx(),
        )
    }
    val hashWidth = minOf(8.dp.toPx(), fieldSize.width * 0.025f)
    val yardHeight = sectionHeight / 10f
    val markingWidth = minOf(1.5.dp.toPx(), yardHeight * 0.4f)
    for (yard in 1 until 100) {
        if (yard % 10 == 0) continue
        val y = endZoneHeight + yardHeight * yard
        if (yard % 5 == 0) {
            drawLine(GameColors.fieldLine, Offset(0f, y), Offset(fieldSize.width, y), markingWidth)
        } else {
            val left = fieldSize.width * 0.375f
            val right = fieldSize.width - left
            drawLine(GameColors.fieldLine, Offset(left - hashWidth / 2f, y), Offset(left + hashWidth / 2f, y), markingWidth)
            drawLine(GameColors.fieldLine, Offset(right - hashWidth / 2f, y), Offset(right + hashWidth / 2f, y), markingWidth)
        }
    }
    for (yard in 1..9) {
        val number = if (yard <= 5) yard * 10 else (10 - yard) * 10
        val text = textMeasurer.measure(number.toString(), style = TextStyle(
            color = GameColors.fieldLine, fontSize = 16.sp, fontWeight = FontWeight.Bold,
        ))
        val y = endZoneHeight + sectionHeight * yard - text.size.height / 2f
        val margin = minOf(20.dp.toPx(), fieldSize.width * 0.04f)
        for (x in listOf(margin, fieldSize.width - margin - text.size.width)) {
            drawRect(
                color = GameColors.arenaSurface,
                topLeft = Offset(x - 3.dp.toPx(), y),
                size = Size(text.size.width + 6.dp.toPx(), text.size.height.toFloat()),
            )
            drawText(text, topLeft = Offset(x, y))
        }
    }
    drawGoalposts(fieldSize.width, endZoneHeight)
    for ((label, centerY) in listOf("RATTLERS!" to endZoneHeight * 0.75f, "START" to fieldSize.height - endZoneHeight / 2f)) {
        val text = textMeasurer.measure(label, style = TextStyle(
            color = GameColors.fieldLine, fontSize = 18.sp, fontWeight = FontWeight.Bold,
        ))
        drawText(text, topLeft = Offset((fieldSize.width - text.size.width) / 2f, centerY - text.size.height / 2f))
    }
}

private fun DrawScope.drawGoalposts(fieldWidth: Float, endZoneHeight: Float) {
    val centerX = fieldWidth / 2f
    val halfWidth = minOf(36.dp.toPx(), fieldWidth * 0.15f)
    val crossbarY = endZoneHeight * 0.33f
    val topY = endZoneHeight * 0.08f
    val stemY = endZoneHeight * 0.5f
    val thickness = 3.dp.toPx()
    drawLine(GameColors.bcuGold, Offset(centerX - halfWidth, topY), Offset(centerX - halfWidth, crossbarY), thickness)
    drawLine(GameColors.bcuGold, Offset(centerX + halfWidth, topY), Offset(centerX + halfWidth, crossbarY), thickness)
    drawLine(GameColors.bcuGold, Offset(centerX - halfWidth, crossbarY), Offset(centerX + halfWidth, crossbarY), thickness)
    drawLine(GameColors.bcuGold, Offset(centerX, crossbarY), Offset(centerX, stemY), thickness)
}

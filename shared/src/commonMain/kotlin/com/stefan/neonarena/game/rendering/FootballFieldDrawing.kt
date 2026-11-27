package com.stefan.neonarena.game.rendering

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.text.font.FontStyle
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
    if (fieldSize.width <= 0f || fieldSize.height <= 0f) return
    drawRect(color = GameColors.arenaSurface, size = fieldSize)
    drawRect(color = GameColors.famuGreen, size = Size(fieldSize.width, endZoneHeight))
    drawRect(
        color = GameColors.famuGreen,
        topLeft = Offset(0f, fieldSize.height - endZoneHeight),
        size = Size(fieldSize.width, endZoneHeight),
    )
    val sectionHeight = (fieldSize.height - 2f * endZoneHeight) / 10f
    for (band in 0 until 20 step 2) {
        drawRect(
            GameColors.turfBand, Offset(0f, endZoneHeight + sectionHeight * band / 2f),
            Size(fieldSize.width, sectionHeight / 2f),
        )
    }
    drawEndZoneStripes(fieldSize.width, endZoneHeight)
    translate(top = fieldSize.height - endZoneHeight) { drawEndZoneStripes(fieldSize.width, endZoneHeight) }
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
            drawRect(
                GameColors.turfBand, Offset(x - 3.dp.toPx(), endZoneHeight + sectionHeight * yard),
                Size(text.size.width + 6.dp.toPx(), text.size.height / 2f),
            )
            drawText(text, topLeft = Offset(x, y))
        }
    }
    drawMidfieldEmblem(fieldSize, sectionHeight, textMeasurer)
    drawGoalposts(fieldSize.width, endZoneHeight)
    for ((label, centerY) in listOf("RATTLERS!" to endZoneHeight * 0.75f, "START" to fieldSize.height - endZoneHeight / 2f)) {
        val text = textMeasurer.measure(label, style = TextStyle(
            color = GameColors.fieldLine, fontSize = 18.sp, fontWeight = FontWeight.Black,
            fontStyle = FontStyle.Italic, letterSpacing = 1.6.sp,
        ))
        drawText(text, topLeft = Offset((fieldSize.width - text.size.width) / 2f, centerY - text.size.height / 2f))
    }
}

private fun DrawScope.drawEndZoneStripes(width: Float, height: Float) {
    val unit = minOf(width * 0.032f, height * 0.45f)
    for (index in 0..2) {
        val x = unit * index * 1.65f
        for (side in 0..1) {
            val origin = if (side == 0) x else width - x
            val step = if (side == 0) unit else -unit
            val stripe = Path().apply {
                moveTo(origin, height)
                lineTo(origin + step, height)
                lineTo(origin + 2.4f * step, 0f)
                lineTo(origin + 1.4f * step, 0f)
                close()
            }
            drawPath(stripe, if (index == 0) GameColors.famuOrange else GameColors.turfBand)
        }
    }
}

private fun DrawScope.drawMidfieldEmblem(field: Size, sectionHeight: Float, textMeasurer: TextMeasurer) {
    val halfWidth = minOf(70.dp.toPx(), field.width * 0.23f, sectionHeight * 0.68f)
    if (halfWidth <= 0f) return
    translate(field.width / 2f, field.height / 2f) {
        val halfHeight = halfWidth * 0.44f
        val diamond = Path().apply {
            moveTo(-halfWidth, 0f)
            lineTo(0f, -halfHeight)
            lineTo(halfWidth, 0f)
            lineTo(0f, halfHeight)
            close()
        }
        drawPath(diamond, GameColors.background)
        drawPath(diamond, GameColors.famuOrange, style = Stroke(width = halfWidth * 0.035f))
        val lettering = textMeasurer.measure("ORLANDO", style = TextStyle(
            color = GameColors.cream, fontWeight = FontWeight.Black, fontStyle = FontStyle.Italic,
            fontSize = (halfWidth * 0.24f).toSp(),
        ))
        drawText(lettering, topLeft = Offset(-lettering.size.width / 2f, -lettering.size.height / 2f))
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

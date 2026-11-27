package com.stefan.neonarena

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.rememberTextMeasurer
import com.stefan.neonarena.game.rendering.drawFootballField
import java.io.File
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Image
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * Renders desktop Compose frames to verify that the real app scrolls yard lines.
 * Saves desktop-sized field previews for inspecting numbering and end-zone art.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
class FieldRenderingTest {
    private val reports = File("build/reports/field-camera").apply { mkdirs() }

    private fun save(image: Image, name: String) {
        image.encodeToData()?.let { File(reports, name).writeBytes(it.bytes) }
    }

    private fun firstYardLine(image: Image): Int {
        val bitmap = Bitmap.makeFromImage(image)
        return try {
            (30 until 350).first { y -> bitmap.getColor(200, y) == 0xFFCEDFD2.toInt() }
        } finally { bitmap.close() }
    }

    @Test
    fun appFrameClockMovesYardLinesDownward() {
        val scene = ImageComposeScene(width = 960, height = 640) { App() }
        try {
            scene.render(0).close()
            scene.render(16_666_667L).close()
            val before = scene.render(33_333_334L)
            val firstLine = firstYardLine(before)
            save(before, "scroll-before.png")
            before.close()
            for (frame in 3..62) scene.render(frame * 16_666_667L).close()
            val after = scene.render(63 * 16_666_667L)
            val movement = firstYardLine(after) - firstLine
            save(after, "scroll-after.png")
            after.close()
            assertTrue(movement in 28..36, "Expected about 32 pixels of scrolling; got $movement")
        } finally { scene.close() }
    }

    @Test
    fun portraitMatchupStaysInPlaceWhileTheFieldScrolls() {
        val scene = ImageComposeScene(width = 360, height = 720) { App() }
        try {
            scene.render(0).close()
            scene.render(16_666_667L).close()
            scene.render(33_333_334L).use { before ->
                save(before, "portrait-drive.png")
                for (frame in 3..62) scene.render(frame * 16_666_667L).close()
                scene.render(63 * 16_666_667L).use { after ->
                    Bitmap.makeFromImage(before).use { first ->
                        Bitmap.makeFromImage(after).use { second ->
                            val header = 0 until 40
                            assertTrue(header.any { y -> (10..50).any { x -> first.getColor(x, y) == 0xFFF4811F.toInt() } })
                            assertTrue(header.all { y -> (0 until 360).all { x -> first.getColor(x, y) == second.getColor(x, y) } })
                        }
                    }
                }
            }
        } finally { scene.close() }
    }

    @Test
    fun farEndZoneRendersGoalpostsAndLettering() {
        val scene = ImageComposeScene(width = 960, height = 640) {
            val textMeasurer = rememberTextMeasurer()
            Canvas(Modifier.fillMaxSize()) {
                drawFootballField(Size(size.width, size.height * 3f), size.height * 0.12f, textMeasurer)
            }
        }
        try {
            val image = scene.render(0)
            save(image, "rattlers-end-zone.png")
            val bitmap = Bitmap.makeFromImage(image)
            try {
                assertTrue((5..40).any { y -> bitmap.getColor(444, y) == 0xFFFDB913.toInt() })
                assertTrue((45..70).any { y -> (410..550).any { x -> bitmap.getColor(x, y) == 0xFFCEDFD2.toInt() } })
            } finally { bitmap.close(); image.close() }
        } finally { scene.close() }
    }
}

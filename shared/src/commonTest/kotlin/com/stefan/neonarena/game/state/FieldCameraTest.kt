package com.stefan.neonarena.game.state

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Checks viewport/world conversions, scrolling bounds, resize progress, and reset.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
class FieldCameraTest {
    @Test fun beginsAtTheNearEndOfAThreeScreenField() {
        val camera = FieldCamera()
        camera.resize(Size(600f, 1000f))
        assertEquals(Size(600f, 3000f), camera.fieldSize)
        assertEquals(2000f, camera.offsetY)
        assertEquals(Offset(100f, 500f), camera.worldToScreen(Offset(100f, 2500f)))
        assertEquals(Offset(100f, 2500f), camera.screenToWorld(Offset(100f, 500f)))
    }
    @Test fun cameraMotionMovesAStationaryWorldMarkDownward() {
        val camera = FieldCamera()
        camera.resize(Size(600f, 1000f))
        val before = camera.worldToScreen(Offset(100f, 2500f))
        assertEquals(32f, camera.advance(32f, 1f))
        assertEquals(before + Offset(0f, 32f), camera.worldToScreen(Offset(100f, 2500f)))
    }
    @Test fun stopsAtTheFarEndAndCanReset() {
        val camera = FieldCamera()
        camera.resize(Size(600f, 1000f))
        assertEquals(2000f, camera.advance(5000f, 1f))
        assertEquals(0f, camera.offsetY)
        assertEquals(0f, camera.advance(32f, 1f))
        camera.reset()
        assertEquals(2000f, camera.offsetY)
    }
    @Test fun resizeKeepsTheSameFractionOfTheJourney() {
        val camera = FieldCamera()
        camera.resize(Size(600f, 1000f))
        camera.advance(160f, 1f)
        camera.resize(Size(400f, 500f))
        assertEquals(920f, camera.offsetY)
    }
    @Test fun frameRateDoesNotChangeTheScrollDistance() {
        for (frames in listOf(30, 120)) {
            val camera = FieldCamera()
            camera.resize(Size(600f, 1000f))
            repeat(frames) { camera.advance(32f, 1f / frames) }
            assertTrue(abs(camera.offsetY - 1968f) < 0.02f)
        }
    }
}

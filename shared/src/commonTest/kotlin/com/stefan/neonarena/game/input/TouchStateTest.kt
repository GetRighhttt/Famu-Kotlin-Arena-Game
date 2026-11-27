package com.stefan.neonarena.game.input

import androidx.compose.ui.geometry.Offset
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Checks drag direction, dead-zone behavior, and clearing touch input.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
class TouchStateTest {
    @Test
    fun directionUsesDisplacementFromTheStartingPoint() {
        val state = TouchState(deadZone = 8f)
        state.start(Offset(200f, 300f))
        state.dragTo(Offset(200f, 280f))
        assertEquals(Offset(0f, -1f), state.direction())
    }

    @Test
    fun movementInsideOrOnTheDeadZoneStaysStill() {
        val state = TouchState(deadZone = 8f)
        state.start(Offset(100f, 100f))
        assertTrue(state.isDragging)
        assertEquals(Offset.Zero, state.direction())
        state.dragTo(Offset(108f, 100f))
        assertEquals(Offset.Zero, state.direction())
        state.dragTo(Offset(109f, 100f))
        assertEquals(Offset(1f, 0f), state.direction())
        state.dragTo(Offset(102f, 100f))
        assertEquals(Offset.Zero, state.direction())
    }

    @Test
    fun fartherDragsKeepTheSameSpeedAndDiagonalLengthIsOne() {
        val state = TouchState(deadZone = 8f)
        state.start(Offset.Zero)
        state.dragTo(Offset(30f, -40f))
        val first = state.direction()
        assertTrue(abs(first.x - 0.6f) < 0.0001f)
        assertTrue(abs(first.y + 0.8f) < 0.0001f)
        assertTrue(abs(first.getDistance() - 1f) < 0.0001f)
        state.dragTo(Offset(300f, -400f))
        assertEquals(first, state.direction())
    }

    @Test
    fun holdingTheSameDisplacementKeepsTheDirectionActive() {
        val state = TouchState(deadZone = 8f)
        state.start(Offset(100f, 100f))
        state.dragTo(Offset(80f, 100f))
        repeat(60) { assertEquals(Offset(-1f, 0f), state.direction()) }
    }

    @Test
    fun clearingStopsMovementAndIgnoresLateDragUpdates() {
        val state = TouchState(deadZone = 8f)
        state.start(Offset.Zero)
        state.dragTo(Offset(20f, 0f))
        state.clear()
        state.dragTo(Offset(40f, 0f))
        assertFalse(state.isDragging)
        assertEquals(Offset.Zero, state.direction())
    }

    @Test
    fun eachNewDragUsesItsOwnStartingPoint() {
        val state = TouchState(deadZone = 8f)
        state.start(Offset.Zero)
        state.dragTo(Offset(100f, 0f))
        state.clear()
        state.start(Offset(500f, 400f))
        state.dragTo(Offset(500f, 430f))
        assertEquals(Offset(0f, 1f), state.direction())
    }
}

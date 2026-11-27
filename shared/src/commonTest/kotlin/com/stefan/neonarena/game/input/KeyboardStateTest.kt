package com.stefan.neonarena.game.input

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.key.Key
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Checks movement directions, key aliases, repeated events, and clearing held keys.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
class KeyboardStateTest {
    @Test
    fun wasdAndArrowKeysChooseTheExpectedDirection() {
        val cases = listOf(
            Key.W to Offset(0f, -1f), Key.DirectionUp to Offset(0f, -1f),
            Key.A to Offset(-1f, 0f), Key.DirectionLeft to Offset(-1f, 0f),
            Key.S to Offset(0f, 1f), Key.DirectionDown to Offset(0f, 1f),
            Key.D to Offset(1f, 0f), Key.DirectionRight to Offset(1f, 0f),
        )
        cases.forEach { (key, expected) ->
            val state = KeyboardState()
            assertTrue(state.press(key))
            assertEquals(expected, state.direction())
            assertTrue(state.release(key))
            assertEquals(Offset.Zero, state.direction())
        }
    }

    @Test
    fun releasingOneAliasKeepsTheOtherHeldKeyActive() {
        val state = KeyboardState()
        state.press(Key.W)
        state.press(Key.DirectionUp)
        state.release(Key.W)
        assertEquals(Offset(0f, -1f), state.direction())
    }

    @Test
    fun repeatedKeyDownEventsNeedOnlyOneRelease() {
        val state = KeyboardState()
        repeat(10) { state.press(Key.D) }
        state.release(Key.D)
        assertEquals(Offset.Zero, state.direction())
    }

    @Test
    fun oppositeKeysCancelUntilOneIsReleased() {
        val state = KeyboardState()
        state.press(Key.A)
        state.press(Key.D)
        assertEquals(Offset.Zero, state.direction())
        state.release(Key.A)
        assertEquals(Offset(1f, 0f), state.direction())
    }

    @Test
    fun diagonalDirectionHasTheSameLengthAsStraightMovement() {
        val state = KeyboardState()
        state.press(Key.W)
        state.press(Key.D)
        val direction = state.direction()
        assertTrue(direction.x > 0f && direction.y < 0f)
        assertTrue(abs(direction.getDistance() - 1f) < 0.0001f)
        state.release(Key.W)
        assertEquals(Offset(1f, 0f), state.direction())
    }

    @Test
    fun unrelatedKeysAreNotHandled() {
        val state = KeyboardState()
        assertFalse(state.press(Key.Spacebar))
        assertFalse(state.release(Key.Spacebar))
        assertEquals(Offset.Zero, state.direction())
    }

    @Test
    fun clearingStateStopsAllMovement() {
        val state = KeyboardState()
        state.press(Key.W)
        state.press(Key.D)
        state.clear()
        assertEquals(Offset.Zero, state.direction())
    }
}

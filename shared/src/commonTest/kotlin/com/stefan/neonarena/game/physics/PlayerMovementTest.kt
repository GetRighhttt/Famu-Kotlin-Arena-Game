package com.stefan.neonarena.game.physics

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.stefan.neonarena.game.model.Player
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Checks movement timing across frame rates and stopping at arena boundaries.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
class PlayerMovementTest {
    private val arena = Size(800f, 1000f)
    private val player = Player(position = Offset(400f, 700f), radius = 18f)

    @Test
    fun velocityMovesThePlayerByDistancePerSecond() {
        val moved = player.movedBy(Offset(80f, -120f), 0.5f, arena)
        assertEquals(Offset(440f, 640f), moved.position)
    }

    @Test
    fun thirtyAndOneHundredTwentyFramesCoverTheSameDistance() {
        fun afterOneSecond(frames: Int): Player {
            var result = player
            repeat(frames) {
                result = result.movedBy(Offset(0f, -120f), 1f / frames, arena)
            }
            return result
        }

        assertTrue(abs(afterOneSecond(30).position.y - 580f) < 0.01f)
        assertTrue(abs(afterOneSecond(120).position.y - 580f) < 0.01f)
    }

    @Test
    fun zeroElapsedTimeDoesNotMoveThePlayer() {
        assertEquals(player, player.movedBy(Offset(0f, -120f), 0f, arena))
    }

    @Test
    fun runnerStopsOneRadiusFromTheTopEdge() {
        val nearTop = player.copy(position = Offset(400f, 20f))
        val moved = nearTop.movedBy(Offset(0f, -120f), 0.05f, arena)
        assertEquals(Offset(400f, 18f), moved.position)
        assertEquals(moved, moved.movedBy(Offset(0f, -120f), 0.05f, arena))
    }
}

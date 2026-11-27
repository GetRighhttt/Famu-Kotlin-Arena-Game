package com.stefan.neonarena.game.physics

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.stefan.neonarena.game.model.Player
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Checks pursuit direction, frame-rate consistency, and arrival without overshoot.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
class DefenderMovementTest {
    private val arena = Size(1000f, 1000f)
    private val defender = Player(Offset(100f, 100f), radius = 18f)

    @Test
    fun diagonalPursuitUsesUnitDirectionAndConstantSpeed() {
        val moved = defender.chasing(Offset(400f, 500f), 100f, 0.5f, arena)
        assertEquals(Offset(130f, 140f), moved.position)
    }

    @Test
    fun arrivalStopsAtTheTargetInsteadOfOvershooting() {
        val target = Offset(103f, 104f)
        val moved = defender.chasing(target, 140f, 0.05f, arena)
        assertEquals(target, moved.position)
        assertEquals(moved, moved.chasing(target, 140f, 0.05f, arena))
    }

    @Test
    fun identicalCentersStayFiniteAndStationary() {
        assertEquals(defender, defender.chasing(defender.position, 140f, 0.05f, arena))
    }

    @Test
    fun changingTheTargetChangesThePursuitDirection() {
        val right = defender.chasing(Offset(500f, 100f), 100f, 0.1f, arena)
        val down = right.chasing(Offset(110f, 500f), 100f, 0.1f, arena)
        assertEquals(Offset(110f, 110f), down.position)
    }

    @Test
    fun thirtyAndOneHundredTwentyUpdatesTravelTheSameDistance() {
        fun simulate(frames: Int): Player {
            var result = defender
            repeat(frames) { result = result.chasing(Offset(900f, 100f), 140f, 1f / frames, arena) }
            return result
        }
        assertTrue(abs(simulate(30).position.x - 240f) < 0.01f)
        assertTrue(abs(simulate(120).position.x - 240f) < 0.01f)
    }

    @Test
    fun pursuitStillRespectsTheArenaBoundary() {
        assertEquals(Offset(18f, 100f), defender.chasing(Offset(-100f, 100f), 200f, 1f, arena).position)
    }
}

package com.stefan.neonarena.game.state

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.stefan.neonarena.game.model.RunStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Checks arena initialization, resize behavior, and runner/defender update ownership.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
class ArenaStateTest {
    @Test
    fun emptyMeasurementWaitsUntilTheFieldHasSpace() {
        val state = ArenaState(18f, fieldScreens = 1f)
        state.resize(Size.Zero)
        state.update(Offset(1f, 0f), 240f, 140f, 0.05f)
        assertFalse(state.isReady)
        state.resize(Size(1000f, 1000f))
        assertTrue(state.isReady)
        assertEquals(Offset(500f, 850f), state.player?.position)
        assertEquals(Offset(500f, 150f), state.defender?.position)
    }

    @Test
    fun tappingMovesOnlyTheRunnerAndConstrainsItsPosition() {
        val state = ArenaState(18f, fieldScreens = 1f)
        state.resize(Size(1000f, 1000f))
        val defender = state.defender
        state.movePlayerTo(Offset(-20f, 1100f))
        assertEquals(Offset(18f, 982f), state.player?.position)
        assertEquals(defender, state.defender)
    }

    @Test
    fun defenderChasesTheRunnersUpdatedPosition() {
        val state = ArenaState(18f, fieldScreens = 1f)
        state.resize(Size(1000f, 1000f))
        state.update(Offset(1f, 0f), 240f, 140f, 0.05f)
        assertEquals(Offset(512f, 850f), state.player?.position)
        val defender = assertNotNull(state.defender)
        assertTrue(defender.position.x > 500f)
        assertTrue(defender.position.y > 150f)
    }

    @Test
    fun defenderMovesEvenWhenRunnerInputIsIdle() {
        val state = ArenaState(18f, fieldScreens = 1f)
        state.resize(Size(1000f, 1000f))
        state.update(Offset.Zero, 240f, 140f, 0.05f)
        assertEquals(Offset(500f, 850f), state.player?.position)
        assertEquals(Offset(500f, 157f), state.defender?.position)
    }

    @Test
    fun resizingConstrainsBothPlayersRatherThanRespawningThem() {
        val state = ArenaState(18f, fieldScreens = 1f)
        state.resize(Size(1000f, 1000f))
        state.resize(Size(200f, 100f))
        assertEquals(Offset(182f, 82f), state.player?.position)
        assertEquals(Offset(182f, 82f), state.defender?.position)
        state.resize(Size(1000f, 1000f))
        assertEquals(Offset(182f, 82f), state.player?.position)
        assertEquals(Offset(182f, 82f), state.defender?.position)
    }

    @Test
    fun tappingIntoTheDefenderTacklesImmediately() {
        val state = ArenaState(18f, fieldScreens = 1f)
        state.resize(Size(1000f, 1000f))
        state.movePlayerTo(assertNotNull(state.defender).position)
        assertEquals(RunStatus.Tackled, state.status)
        assertFalse(state.isRunning)
    }

    @Test
    fun defenderContactEndsTheRunDuringAFrameUpdate() {
        val state = ArenaState(18f, fieldScreens = 1f)
        state.resize(Size(1000f, 1000f))
        state.movePlayerTo(Offset(500f, 190f))
        assertEquals(RunStatus.Running, state.status)
        state.update(Offset.Zero, 240f, 140f, 0.05f)
        assertEquals(RunStatus.Tackled, state.status)
    }

    @Test
    fun runnerContactIsCheckedBeforeDefenderMovement() {
        val state = ArenaState(18f, fieldScreens = 1f)
        state.resize(Size(1000f, 1000f))
        state.movePlayerTo(Offset(500f, 190f))
        state.update(Offset(0f, -1f), 240f, 140f, 0.05f)
        assertEquals(RunStatus.Tackled, state.status)
        assertEquals(Offset(500f, 150f), state.defender?.position)
    }

    @Test
    fun tackledRunIgnoresUpdatesAndTapMovement() {
        val state = ArenaState(18f, fieldScreens = 1f)
        state.resize(Size(1000f, 1000f))
        state.movePlayerTo(assertNotNull(state.defender).position)
        val runner = state.player
        val defender = state.defender
        state.movePlayerTo(Offset(900f, 900f))
        state.update(Offset(1f, 0f), 240f, 140f, 0.05f)
        assertEquals(runner, state.player)
        assertEquals(defender, state.defender)
    }

    @Test
    fun restartResetsPositionsAndAllowsMovementAgain() {
        val state = ArenaState(18f, fieldScreens = 1f)
        state.resize(Size(1000f, 1000f))
        state.movePlayerTo(assertNotNull(state.defender).position)
        state.restart()
        assertEquals(RunStatus.Running, state.status)
        assertTrue(state.isRunning)
        assertEquals(Offset(500f, 850f), state.player?.position)
        assertEquals(Offset(500f, 150f), state.defender?.position)
        state.update(Offset(1f, 0f), 240f, 140f, 0.05f)
        assertEquals(Offset(512f, 850f), state.player?.position)
    }

    @Test
    fun resizingPlayersIntoContactAlsoEndsTheRun() {
        val state = ArenaState(18f, fieldScreens = 1f)
        state.resize(Size(1000f, 1000f))
        state.resize(Size(40f, 40f))
        assertEquals(RunStatus.Tackled, state.status)
    }
}

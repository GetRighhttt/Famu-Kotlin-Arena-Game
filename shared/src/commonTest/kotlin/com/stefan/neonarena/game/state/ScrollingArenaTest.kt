package com.stefan.neonarena.game.state

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.stefan.neonarena.game.model.RunStatus
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Checks world-space gameplay under camera movement and touchdown transitions.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
class ScrollingArenaTest {
    private fun arena() = ArenaState(18f).apply { resize(Size(600f, 1000f)) }
    @Test fun idleRunnerStaysAtTheSameScreenPositionWhileTheFieldScrolls() {
        val state = arena()
        val before = state.camera.worldToScreen(assertNotNull(state.player).position)
        state.update(Offset.Zero, 240f, 0f, 0.05f, scrollSpeed = 32f)
        val after = state.camera.worldToScreen(assertNotNull(state.player).position)
        assertTrue(abs(before.y - after.y) < 0.001f)
        assertTrue(state.camera.offsetY < state.camera.maxOffset)
    }
    @Test fun screenTapIsConvertedToTheCorrectWorldPosition() {
        val state = arena()
        val screenPosition = Offset(100f, 700f)
        state.movePlayerTo(state.camera.screenToWorld(screenPosition))
        assertEquals(screenPosition, state.camera.worldToScreen(assertNotNull(state.player).position))
    }
    @Test fun runnerCannotSkipTheCameraJourneyByMovingToHiddenWorldCoordinates() {
        val state = arena()
        state.movePlayerTo(Offset(100f, 18f))
        assertEquals(RunStatus.Running, state.status)
        assertTrue(assertNotNull(state.player).position.y > state.camera.endZoneHeight)
    }
    @Test fun reachingTheRevealedFarEndZoneWinsAndFreezesTheRun() {
        val state = arena()
        state.update(Offset.Zero, 0f, 0f, 1f, scrollSpeed = 2000f)
        state.movePlayerTo(Offset(100f, 100f))
        assertEquals(RunStatus.Touchdown, state.status)
        val winner = state.player
        state.update(Offset(1f, 0f), 240f, 140f, 0.05f, scrollSpeed = 32f)
        assertEquals(winner, state.player)
        assertEquals(0f, state.camera.offsetY)
        state.restart()
        assertEquals(RunStatus.Running, state.status)
        assertEquals(2000f, state.camera.offsetY)
        assertEquals(Offset(300f, 2850f), state.player?.position)
    }
    @Test fun tackleWinsWhenContactAndTheGoalOccurTogether() {
        val state = ArenaState(18f, fieldScreens = 1f)
        state.resize(Size(600f, 1000f))
        state.movePlayerTo(Offset(300f, 136f))
        assertEquals(RunStatus.Tackled, state.status)
    }
    @Test fun tackleAlsoFreezesTheCamera() {
        val state = arena()
        state.movePlayerTo(assertNotNull(state.defender).position)
        val offset = state.camera.offsetY
        state.update(Offset.Zero, 240f, 140f, 1f, scrollSpeed = 32f)
        assertEquals(offset, state.camera.offsetY)
    }
}

package com.stefan.neonarena

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.stefan.neonarena.game.input.KeyboardState
import com.stefan.neonarena.game.input.TouchState
import com.stefan.neonarena.game.input.keyboardControls
import com.stefan.neonarena.game.loop.GameLoop
import com.stefan.neonarena.game.model.RunStatus
import com.stefan.neonarena.game.state.ArenaState
import com.stefan.neonarena.ui.arena.Arena

/**
 * Connects input, frame timing, and arena state for the runner and chasing defender.
 * Screen-specific dp values become pixels here before entering game calculations.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
@Composable
fun App() {
    val playerRadius = with(LocalDensity.current) { 18.dp.toPx() }
    val runningSpeed = with(LocalDensity.current) { 240.dp.toPx() }
    val defenderSpeed = with(LocalDensity.current) { 140.dp.toPx() }
    val arenaState = remember(playerRadius) { ArenaState(playerRadius) }
    val keyboardState = remember { KeyboardState() }
    val touchDeadZone = with(LocalDensity.current) { 8.dp.toPx() }
    val touchState = remember(touchDeadZone) { TouchState(touchDeadZone) }

    GameLoop(running = arenaState.isRunning) { elapsedSeconds ->
        val direction = if (touchState.isDragging) touchState.direction() else keyboardState.direction()
        arenaState.update(direction, runningSpeed, defenderSpeed, elapsedSeconds)
    }

    // Recreate measurement callbacks if display density creates a new arena state.
    key(arenaState) {
        Arena(
            player = arenaState.player,
            defender = arenaState.defender,
            touchState = touchState,
            isRunning = arenaState.isRunning,
            isTackled = arenaState.status == RunStatus.Tackled,
            modifier = if (arenaState.isRunning) Modifier.keyboardControls(keyboardState) else Modifier,
            onRestart = {
                keyboardState.clear()
                touchState.clear()
                arenaState.restart()
            },
            onArenaSizeChanged = arenaState::resize,
            onTap = { position, _ -> arenaState.movePlayerTo(position) },
        )
    }
}

package com.stefan.neonarena

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.stefan.neonarena.game.input.TouchState
import com.stefan.neonarena.game.input.KeyboardState
import com.stefan.neonarena.game.input.keyboardControls
import com.stefan.neonarena.game.model.Player
import com.stefan.neonarena.game.loop.GameLoop
import com.stefan.neonarena.game.physics.movedBy
import com.stefan.neonarena.ui.arena.Arena

/**
 * Owns player and input state, applying touch or keyboard direction each frame.
 * Tap and resize events apply the same arena boundaries as automatic movement.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
@Composable
fun App() {
    val playerRadius = with(LocalDensity.current) { 18.dp.toPx() }
    val runningSpeed = with(LocalDensity.current) { 240.dp.toPx() }
    var player by remember(playerRadius) { mutableStateOf<Player?>(null) }
    var arenaSize by remember { mutableStateOf(Size.Zero) }
    val keyboardState = remember { KeyboardState() }
    val touchDeadZone = with(LocalDensity.current) { 8.dp.toPx() }
    val touchState = remember(touchDeadZone) { TouchState(touchDeadZone) }

    GameLoop(running = player != null && arenaSize.width > 0f && arenaSize.height > 0f) { elapsedSeconds ->
        val direction = if (touchState.isDragging) touchState.direction() else keyboardState.direction()
        player = player?.movedBy(
            velocity = direction * runningSpeed,
            elapsedSeconds = elapsedSeconds,
            arenaSize = arenaSize,
        )
    }

    Arena(
        player = player,
        touchState = touchState,
        modifier = Modifier.keyboardControls(keyboardState),
        onArenaSizeChanged = { bounds ->
            arenaSize = bounds
            player = (player ?: Player(
                position = Offset(bounds.width / 2f, bounds.height * 0.85f),
                radius = playerRadius,
            )).constrainedTo(bounds)
        },
        onTap = { position, bounds ->
            player = Player(position, playerRadius).constrainedTo(bounds)
        },
    )
}

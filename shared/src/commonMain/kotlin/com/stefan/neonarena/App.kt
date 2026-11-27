package com.stefan.neonarena

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.stefan.neonarena.game.model.Player
import com.stefan.neonarena.game.loop.GameLoop
import com.stefan.neonarena.game.physics.movedBy
import com.stefan.neonarena.ui.arena.Arena

/**
 * Owns player state and advances the runner upfield on each game-loop update.
 * Tap and resize events apply the same arena boundaries as automatic movement.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
@Composable
fun App() {
    val playerRadius = with(LocalDensity.current) { 18.dp.toPx() }
    val runningSpeed = with(LocalDensity.current) { 120.dp.toPx() }
    var player by remember(playerRadius) { mutableStateOf<Player?>(null) }
    var arenaSize by remember { mutableStateOf(Size.Zero) }

    GameLoop(running = player != null && arenaSize.width > 0f && arenaSize.height > 0f) { elapsedSeconds ->
        player = player?.movedBy(
            velocity = Offset(x = 0f, y = -runningSpeed),
            elapsedSeconds = elapsedSeconds,
            arenaSize = arenaSize,
        )
    }

    Arena(
        player = player,
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

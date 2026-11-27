package com.stefan.neonarena

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.stefan.neonarena.game.model.Player
import com.stefan.neonarena.ui.arena.Arena

/**
 * Owns the player state and applies arena bounds to input and size changes.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
@Composable
fun App() {
    val playerRadius = with(LocalDensity.current) { 18.dp.toPx() }
    var player by remember(playerRadius) { mutableStateOf<Player?>(null) }

    Arena(
        player = player,
        onArenaSizeChanged = { bounds ->
            player = (player ?: Player(
                position = Offset(bounds.width / 20f, bounds.height / 2f),
                radius = playerRadius,
            )).constrainedTo(bounds)
        },
        onTap = { position, bounds ->
            player = Player(position, playerRadius).constrainedTo(bounds)
        },
    )
}

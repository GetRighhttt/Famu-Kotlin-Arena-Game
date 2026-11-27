package com.stefan.neonarena.game.physics

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.stefan.neonarena.game.model.Player

/**
 * Advances a player using velocity in pixels per second and elapsed seconds.
 * The returned player stays inside the arena using the existing radius rules.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
fun Player.movedBy(velocity: Offset, elapsedSeconds: Float, arenaSize: Size): Player =
    copy(position = position + velocity * elapsedSeconds).constrainedTo(arenaSize)

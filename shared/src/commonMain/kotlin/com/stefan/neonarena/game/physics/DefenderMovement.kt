package com.stefan.neonarena.game.physics

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.stefan.neonarena.game.model.Player

/**
 * Moves a defender toward a target center at a speed in pixels per second.
 *
 * Normalizing the target displacement gives direction independently of distance.
 * Travel is limited to the remaining distance to prevent overshooting. Matching
 * centers need no normalization, which avoids dividing by zero. ArenaState owns
 * contact outcomes; this function only calculates pursuit.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
fun Player.chasing(target: Offset, speed: Float, elapsedSeconds: Float, arenaSize: Size): Player {
    val displacement = target - position
    val distance = displacement.getDistance()
    if (distance == 0f) return constrainedTo(arenaSize)

    val direction = displacement / distance
    val travel = minOf(speed * elapsedSeconds, distance)
    return copy(position = position + direction * travel).constrainedTo(arenaSize)
}

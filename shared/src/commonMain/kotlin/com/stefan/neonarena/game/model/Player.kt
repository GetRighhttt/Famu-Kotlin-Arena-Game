package com.stefan.neonarena.game.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size

/**
 * Holds player geometry in pixels and constrains its center to the arena.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
data class Player(
    val position: Offset,
    val radius: Float,
) {
    // A very small window can be narrower than the player's diameter.
    fun radiusIn(arenaSize: Size): Float =
        minOf(radius, arenaSize.minDimension / 2f).coerceAtLeast(0f)

    fun constrainedTo(arenaSize: Size): Player {
        val visibleRadius = radiusIn(arenaSize)
        return copy(
            position = Offset(
                x = position.x.coerceIn(visibleRadius, arenaSize.width - visibleRadius),
                y = position.y.coerceIn(visibleRadius, arenaSize.height - visibleRadius),
            ),
        )
    }
}

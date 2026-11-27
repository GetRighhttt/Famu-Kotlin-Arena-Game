package com.stefan.neonarena.game.physics

import androidx.compose.ui.geometry.Size
import com.stefan.neonarena.game.model.Player

/**
 * Checks whether two visible player circles touch or overlap inside the arena.
 * Squared distance avoids a square root; equality counts as first contact.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
fun Player.touches(other: Player, arenaSize: Size): Boolean {
    val combinedRadius = radiusIn(arenaSize) + other.radiusIn(arenaSize)
    return (position - other.position).getDistanceSquared() <= combinedRadius * combinedRadius
}

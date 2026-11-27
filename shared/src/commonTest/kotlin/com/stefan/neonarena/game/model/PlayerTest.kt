package com.stefan.neonarena.game.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Checks player bounds for normal, resized, and undersized arenas.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
class PlayerTest {
    private val arena = Size(200f, 100f)

    @Test
    fun interiorPositionStaysTheSame() {
        val player = Player(Offset(50f, 60f), radius = 18f)
        assertEquals(player, player.constrainedTo(arena))
    }

    @Test
    fun eachCornerLeavesRoomForTheRadius() {
        val cases = listOf(
            Offset(-10f, -10f) to Offset(18f, 18f),
            Offset(210f, -10f) to Offset(182f, 18f),
            Offset(-10f, 110f) to Offset(18f, 82f),
            Offset(210f, 110f) to Offset(182f, 82f),
        )
        cases.forEach { (requested, expected) ->
            assertEquals(expected, Player(requested, 18f).constrainedTo(arena).position)
        }
    }

    @Test
    fun shrinkingTheArenaMovesThePlayerInside() {
        val player = Player(Offset(182f, 82f), radius = 18f)
        assertEquals(Offset(62f, 42f), player.constrainedTo(Size(80f, 60f)).position)
    }

    @Test
    fun narrowArenaReducesVisibleRadiusWithoutChangingPlayerRadius() {
        val bounds = Size(20f, 10f)
        val player = Player(Offset(200f, 100f), radius = 18f).constrainedTo(bounds)
        assertEquals(Offset(15f, 5f), player.position)
        assertEquals(5f, player.radiusIn(bounds))
        assertEquals(18f, player.radius)
        assertEquals(18f, player.radiusIn(arena))
    }

    @Test
    fun emptyArenaHasZeroVisibleRadiusAndPosition() {
        val player = Player(Offset(50f, 60f), radius = 18f).constrainedTo(Size.Zero)
        assertEquals(Offset.Zero, player.position)
        assertEquals(0f, player.radiusIn(Size.Zero))
    }
}

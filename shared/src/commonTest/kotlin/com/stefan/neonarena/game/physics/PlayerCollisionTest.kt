package com.stefan.neonarena.game.physics

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.stefan.neonarena.game.model.Player
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Checks circle contact at exact boundaries and with arena-adjusted visible radii.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
class PlayerCollisionTest {
    private val arena = Size(1000f, 1000f)
    private val runner = Player(Offset(100f, 100f), 18f)

    @Test
    fun exactContactCountsAsATackleButAGapDoesNot() {
        assertTrue(runner.touches(Player(Offset(136f, 100f), 18f), arena))
        assertFalse(runner.touches(Player(Offset(136.1f, 100f), 18f), arena))
    }

    @Test
    fun diagonalDistanceUsesBothCoordinates() {
        val player = Player(Offset(100f, 100f), 20f)
        assertTrue(player.touches(Player(Offset(124f, 132f), 20f), arena))
        assertFalse(player.touches(Player(Offset(130f, 140f), 20f), arena))
    }

    @Test
    fun overlapAndIdenticalCentersCountAsContact() {
        assertTrue(runner.touches(Player(Offset(110f, 100f), 10f), arena))
        assertTrue(runner.touches(runner, arena))
    }

    @Test
    fun smallArenaUsesTheVisibleRadii() {
        val small = Size(100f, 10f)
        assertFalse(Player(Offset(10f, 5f), 18f).touches(Player(Offset(25f, 5f), 18f), small))
    }
}

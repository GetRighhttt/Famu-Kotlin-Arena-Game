package com.stefan.neonarena.game.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.stefan.neonarena.game.model.RunStatus
import com.stefan.neonarena.game.model.Player
import com.stefan.neonarena.game.physics.chasing
import com.stefan.neonarena.game.physics.movedBy
import com.stefan.neonarena.game.physics.touches

/**
 * Owns the runner, defender, and measured field bounds for one arena composition.
 *
 * Resize creates the initial players or constrains existing positions. A frame
 * moves the runner first, then directs the defender toward its updated position.
 * Contact ends the run; restart restores both initial positions.
 * Both football players reuse the Player geometry model. All coordinates and
 * speeds passed here use pixels; App performs density conversion.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
class ArenaState(private val playerRadius: Float) {
    var player by mutableStateOf<Player?>(null)
        private set
    var defender by mutableStateOf<Player?>(null)
        private set
    var size by mutableStateOf(Size.Zero)
        private set

    var status by mutableStateOf(RunStatus.Running)
        private set

    val isRunning: Boolean
        get() = isReady && status == RunStatus.Running

    val isReady: Boolean
        get() = player != null && defender != null && size.width > 0f && size.height > 0f

    fun resize(bounds: Size) {
        size = bounds
        if (bounds.width <= 0f || bounds.height <= 0f) return
        player = (player ?: Player(Offset(bounds.width / 2f, bounds.height * 0.85f), playerRadius))
            .constrainedTo(bounds)
        defender = (defender ?: Player(Offset(bounds.width / 2f, bounds.height * 0.15f), playerRadius))
            .constrainedTo(bounds)
        checkForTackle()
    }

    fun movePlayerTo(position: Offset) {
        if (!isRunning) return
        player = player?.copy(position = position)?.constrainedTo(size)
        checkForTackle()
    }

    fun restart() {
        player = null
        defender = null
        status = RunStatus.Running
        resize(size)
    }

    fun update(direction: Offset, runnerSpeed: Float, defenderSpeed: Float, elapsedSeconds: Float) {
        if (!isRunning) return
        checkForTackle()
        if (!isRunning) return
        val runner = player ?: return
        val opponent = defender ?: return
        val movedRunner = runner.movedBy(direction * runnerSpeed, elapsedSeconds, size)
        player = movedRunner
        // Check the runner's move before moving the defender away from that contact.
        checkForTackle()
        if (!isRunning) return
        defender = opponent.chasing(movedRunner.position, defenderSpeed, elapsedSeconds, size)
        checkForTackle()
    }

    private fun checkForTackle() {
        if (!isReady || status != RunStatus.Running) return
        val runner = player ?: return
        val opponent = defender ?: return
        if (runner.touches(opponent, size)) status = RunStatus.Tackled
    }
}

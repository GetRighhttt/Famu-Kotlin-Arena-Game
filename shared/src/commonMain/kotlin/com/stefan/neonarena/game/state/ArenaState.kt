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
 * World coordinates drive movement and contact. The camera carries the runner
 * forward while input steers within the viewport. Contact or reaching the far
 * end zone ends the run; restart restores positions and camera progress.
 * Both football players reuse the Player geometry model. All coordinates and
 * speeds passed here use pixels; App performs density conversion.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
class ArenaState(private val playerRadius: Float, fieldScreens: Float = 3f) {
    val camera = FieldCamera(fieldScreens)
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

    /**
     * A resize changes the camera offset as well as the visible bounds.
     * Shift existing world positions by that offset change to keep their screen y
     * positions steady. On the first measurement, create the runner near the bottom
     * and the defender near the top. Then clamp both to their allowed areas.
     */
    fun resize(bounds: Size) {
        if (bounds.width <= 0f || bounds.height <= 0f) {
            size = bounds
            return
        }
        val oldOffset = camera.offsetY
        camera.resize(bounds)
        size = bounds
        val offsetChange = Offset(0f, camera.offsetY - oldOffset)
        player = (player?.let { it.copy(position = it.position + offsetChange) } ?: Player(
            camera.screenToWorld(Offset(bounds.width / 2f, bounds.height * 0.85f)), playerRadius,
        )).withinViewport()
        defender = (defender?.let { it.copy(position = it.position + offsetChange) } ?: Player(
            camera.screenToWorld(Offset(bounds.width / 2f, bounds.height * 0.15f)), playerRadius,
        )).constrainedTo(camera.fieldSize)
        checkForOutcome()
    }

    /**
     * Convert to screen coordinates before applying the visible boundary rules.
     * Convert back afterward because movement and collision checks use world positions.
     * The returned copy prevents the runner from steering into the hidden field.
     */
    private fun Player.withinViewport(): Player {
        val visible = copy(position = camera.worldToScreen(position)).constrainedTo(size)
        return visible.copy(position = camera.screenToWorld(visible.position))
    }

    /**
     * Accepts a world position. App converts a local tap with screenToWorld first,
     * so tapping at screen y = 100 with a camera offset of 1200 targets world y = 1300.
     */
    fun movePlayerTo(position: Offset) {
        if (!isRunning) return
        player = player?.copy(position = position)?.withinViewport()
        checkForOutcome()
    }

    /**
     * Recreate both players after returning the camera to the near end.
     * App clears held input before calling this method, so the new run starts with no held steering.
     */
    fun restart() {
        camera.reset()
        player = null
        defender = null
        status = RunStatus.Running
        resize(size)
    }

    /**
     * Updates one frame using seconds and speeds in pixels per second.
     * A direction of length one requests full speed. Zero means no steering.
     *
     * First advance the camera, then subtract its actual travel from the runner's
     * world y. That keeps an idle runner at the same screen y as the field scrolls.
     * Steering adds direction * runnerSpeed * elapsedSeconds to that world position.
     * The defender then chases the runner's new position.
     *
     * Check contact between movement steps so either player's move can end the run.
     */
    fun update(direction: Offset, runnerSpeed: Float, defenderSpeed: Float, elapsedSeconds: Float, scrollSpeed: Float = 0f) {
        if (!isRunning) return
        checkForOutcome()
        if (!isRunning) return
        val runner = player ?: return
        val opponent = defender ?: return
        val cameraTravel = camera.advance(scrollSpeed, elapsedSeconds)
        val carriedRunner = runner.copy(position = runner.position - Offset(0f, cameraTravel))
        val movedRunner = carriedRunner.movedBy(direction * runnerSpeed, elapsedSeconds, camera.fieldSize)
            .withinViewport()
        player = movedRunner
        // Check the runner's move before moving the defender away from that contact.
        checkForOutcome()
        if (!isRunning) return
        defender = opponent.chasing(movedRunner.position, defenderSpeed, elapsedSeconds, camera.fieldSize)
        checkForOutcome()
    }

    /**
     * Contact takes priority when a frame also reaches the goal line.
     * The runner's leading edge is center y minus radius because upfield is negative y.
     * A touchdown requires the camera to finish its travel, which keeps the runner
     * on the visible field until the far end zone is fully revealed.
     */
    private fun checkForOutcome() {
        if (!isReady || status != RunStatus.Running) return
        val runner = player ?: return
        val opponent = defender ?: return
        status = when {
            runner.touches(opponent, size) -> RunStatus.Tackled
            camera.offsetY == 0f && runner.position.y - runner.radiusIn(size) <= camera.endZoneHeight -> RunStatus.Touchdown
            else -> RunStatus.Running
        }
    }
}

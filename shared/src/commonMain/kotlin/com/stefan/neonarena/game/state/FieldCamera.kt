package com.stefan.neonarena.game.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size

/**
 * Views a taller field through a screen-sized viewport, starting at its near end.
 *
 * The offset is the world y-coordinate at the top of the viewport. Decreasing
 * it reveals the far end: screenY = worldY - offsetY. Speed is pixels per second.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
class FieldCamera(private val fieldScreens: Float = 3f) {
    var viewport by mutableStateOf(Size.Zero)
        private set
    var offsetY by mutableStateOf(0f)
        private set
    val fieldSize: Size get() = Size(viewport.width, viewport.height * fieldScreens)
    val maxOffset: Float get() = fieldSize.height - viewport.height
    val endZoneHeight: Float get() = viewport.height * 0.12f

    /**
     * Keep the same fraction of camera travel remaining after a resize.
     * For example, halfway through a 1200-pixel range becomes halfway through a
     * 1600-pixel range. The initial measurement starts at the near end.
     */
    fun resize(bounds: Size) {
        val fraction = if (maxOffset > 0f) offsetY / maxOffset else 1f
        viewport = bounds
        offsetY = maxOffset * fraction
    }

    /**
     * Returns actual camera travel in pixels for this frame.
     * Near zero, the camera may have less distance left than speed * elapsedSeconds.
     * The runner uses this clamped travel so both stop advancing together.
     */
    fun advance(speed: Float, elapsedSeconds: Float): Float {
        val previous = offsetY
        offsetY = (offsetY - speed * elapsedSeconds).coerceIn(0f, maxOffset)
        return previous - offsetY
    }

    fun reset() { offsetY = maxOffset }
    /**
     * A world point at y = 1400 appears at screen y = 200 when offsetY is 1200.
     */
    fun worldToScreen(position: Offset): Offset = position - Offset(0f, offsetY)
    /**
     * Reverse the camera translation for input measured inside the visible canvas.
     */
    fun screenToWorld(position: Offset): Offset = position + Offset(0f, offsetY)
}

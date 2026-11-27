package com.stefan.neonarena.game.input

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset

/**
 * Converts displacement from a drag's starting point into a unit direction.
 *
 * The dead zone is in local pixels. Moving back into it stops touch movement;
 * moving farther away changes direction but does not increase running speed.
 * The screen owns this state and clears it on release or cancellation.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
class TouchState(private val deadZone: Float) {
    var isDragging by mutableStateOf(false)
        private set
    private var anchor = Offset.Zero
    private var displacement by mutableStateOf(Offset.Zero)

    fun start(position: Offset) {
        anchor = position
        displacement = Offset.Zero
        isDragging = true
    }

    fun dragTo(position: Offset) {
        if (isDragging) displacement = position - anchor
    }

    fun clear() {
        isDragging = false
        displacement = Offset.Zero
    }

    /**
     * Treat the drag as a direction from the original finger-down position.
     * Inside the dead zone, return zero. Outside it, dividing by distance gives a
     * unit vector, so dragging farther changes the heading without increasing speed.
     */
    fun direction(): Offset {
        val distance = displacement.getDistance()
        return if (!isDragging || distance <= deadZone || distance == 0f) {
            Offset.Zero
        } else {
            displacement / distance
        }
    }
}

package com.stefan.neonarena.game.input

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.key.Key

/**
 * Tracks held movement keys and converts them to a direction of length at most one.
 *
 * Each physical key is stored separately so releasing W does not cancel a held
 * Up arrow. Opposing directions cancel; diagonal directions are normalized to
 * keep their speed equal to straight movement. The screen owns this state.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
class KeyboardState {
    private var heldKeys by mutableStateOf(emptySet<Key>())

    fun press(key: Key): Boolean {
        if (key !in movementKeys) return false
        heldKeys = heldKeys + key
        return true
    }

    fun release(key: Key): Boolean {
        if (key !in movementKeys) return false
        heldKeys = heldKeys - key
        return true
    }

    fun clear() {
        heldKeys = emptySet()
    }

    fun direction(): Offset {
        val left = Key.A in heldKeys || Key.DirectionLeft in heldKeys
        val right = Key.D in heldKeys || Key.DirectionRight in heldKeys
        val up = Key.W in heldKeys || Key.DirectionUp in heldKeys
        val down = Key.S in heldKeys || Key.DirectionDown in heldKeys
        val direction = Offset(
            x = (if (right) 1f else 0f) - (if (left) 1f else 0f),
            y = (if (down) 1f else 0f) - (if (up) 1f else 0f),
        )
        return if (direction == Offset.Zero) direction else direction / direction.getDistance()
    }

    private companion object {
        val movementKeys = setOf(
            Key.W, Key.A, Key.S, Key.D,
            Key.DirectionUp, Key.DirectionLeft, Key.DirectionDown, Key.DirectionRight,
        )
    }
}

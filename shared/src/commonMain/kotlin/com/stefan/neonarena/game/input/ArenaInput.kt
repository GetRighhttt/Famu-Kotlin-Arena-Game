package com.stefan.neonarena.game.input

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.toSize

/**
 * Reports taps and arena bounds in the same local pixel coordinate system.
 * Apply after arena padding so input coordinates match the drawing area.
 * The callback stays current without restarting a gesture during frame updates.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
@Composable
fun Modifier.onArenaTap(onTap: (Offset, Size) -> Unit): Modifier {
    val currentOnTap by rememberUpdatedState(onTap)
    return pointerInput(Unit) {
        detectTapGestures { position ->
            currentOnTap(position, size.toSize())
        }
    }
}

package com.stefan.neonarena.game.input

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.toSize

/**
 * Reports taps and arena bounds in the same local pixel coordinate system.
 * Apply after arena padding so input coordinates match the drawing area.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
fun Modifier.onArenaTap(onTap: (Offset, Size) -> Unit): Modifier =
    pointerInput(onTap) {
        detectTapGestures { position ->
            onTap(position, size.toSize())
        }
    }

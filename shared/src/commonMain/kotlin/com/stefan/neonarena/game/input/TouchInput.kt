package com.stefan.neonarena.game.input

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalWindowInfo

/**
 * Connects drag gestures to touch steering without taking over simple taps.
 *
 * The initial pointer-down position anchors the control once drag recognition
 * passes touch slop. The drag consumes movement so the tap handler cancels.
 * Release, cancellation, window focus loss, and disposal clear touch movement.
 * Mouse dragging also works for trying this lesson on desktop.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
@Composable
fun Modifier.touchControls(state: TouchState): Modifier {
    val windowFocused = LocalWindowInfo.current.isWindowFocused
    LaunchedEffect(windowFocused, state) {
        if (!windowFocused) state.clear()
    }
    DisposableEffect(state) {
        onDispose { state.clear() }
    }

    return pointerInput(state) {
        detectDragGestures(
            orientationLock = null,
            onDragStart = { down, _, _ -> state.start(down.position) },
            onDragEnd = { state.clear() },
            onDragCancel = { state.clear() },
            onDrag = { change, _ ->
                change.consume()
                state.dragTo(change.position)
            },
        )
    }
}

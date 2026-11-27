package com.stefan.neonarena.game.input

import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isAltPressed
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalWindowInfo

/**
 * Connects arena focus and hardware keyboard events to the screen's held-key state.
 *
 * Focus is requested on entry and pointer presses. Pointer events are observed
 * without consuming them, so arena taps can still reposition the runner. Losing
 * arena or window focus clears held keys to prevent movement after returning.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
@Composable
fun Modifier.keyboardControls(state: KeyboardState): Modifier {
    val focusRequester = remember { FocusRequester() }
    val windowFocused = LocalWindowInfo.current.isWindowFocused

    LaunchedEffect(focusRequester) { focusRequester.requestFocus() }
    LaunchedEffect(windowFocused, state) {
        if (!windowFocused) state.clear()
    }
    DisposableEffect(state) {
        onDispose { state.clear() }
    }

    return this
        .focusRequester(focusRequester)
        .onFocusChanged { if (!it.isFocused) state.clear() }
        .onKeyEvent { event ->
            when (event.type) {
                KeyEventType.KeyUp -> state.release(event.key)
                KeyEventType.KeyDown -> {
                    if (event.isCtrlPressed || event.isMetaPressed || event.isAltPressed) {
                        state.clear()
                        false
                    } else {
                        state.press(event.key)
                    }
                }
                else -> false
            }
        }
        .focusable()
        .pointerInput(state, focusRequester) {
            awaitEachGesture {
                awaitFirstDown(requireUnconsumed = false)
                focusRequester.requestFocus()
            }
        }
}

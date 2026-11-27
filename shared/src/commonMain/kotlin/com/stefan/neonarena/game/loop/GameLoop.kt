package com.stefan.neonarena.game.loop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.withFrameNanos
import kotlinx.coroutines.isActive

/**
 * Calls the latest update callback once per frame while the game is running.
 *
 * The first frame establishes a timestamp. Later frames report elapsed seconds,
 * capped at 0.05 so a long stall does not cause a large movement jump. Waiting
 * for the frame clock suspends the coroutine rather than blocking the UI thread.
 * Leaving composition or changing running cancels the current loop.
 *
 * LaunchedEffect starts a coroutine tied to the running value. Each suspension
 * resumes at a frame timestamp in nanoseconds. Divide the difference by one
 * billion to get seconds. At 60 frames per second, 240 pixels per second moves
 * about 4 pixels per update. The cap discards excess time after a long stall.
 * rememberUpdatedState lets this coroutine call the latest callback without
 * restarting its timestamp each time App recomposes.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
@Composable
fun GameLoop(running: Boolean, onUpdate: (elapsedSeconds: Float) -> Unit) {
    val currentOnUpdate by rememberUpdatedState(onUpdate)

    LaunchedEffect(running) {
        if (!running) return@LaunchedEffect

        var previousFrame = withFrameNanos { it }
        while (isActive) {
            val currentFrame = withFrameNanos { it }
            val elapsedSeconds = ((currentFrame - previousFrame) / 1_000_000_000f)
                .coerceIn(0f, 0.05f)
            previousFrame = currentFrame
            currentOnUpdate(elapsedSeconds)
        }
    }
}

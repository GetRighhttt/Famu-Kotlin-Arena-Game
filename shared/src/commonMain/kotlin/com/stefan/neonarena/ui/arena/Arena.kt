package com.stefan.neonarena.ui.arena

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.stefan.neonarena.game.model.Player
import com.stefan.neonarena.game.input.onArenaTap
import com.stefan.neonarena.game.input.TouchState
import com.stefan.neonarena.game.input.touchControls
import com.stefan.neonarena.game.rendering.drawFootballField
import com.stefan.neonarena.game.rendering.drawPlayer
import com.stefan.neonarena.ui.theme.GameColors

/**
 * Composes the football field and player, and reports size and tap events.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
@Composable
fun Arena(
    player: Player?,
    touchState: TouchState,
    onArenaSizeChanged: (Size) -> Unit,
    onTap: (Offset, Size) -> Unit,
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(GameColors.background)
            .safeDrawingPadding()
            .padding(24.dp)
            .border(1.dp, GameColors.famuGreen)
            .clipToBounds()
            .onSizeChanged { onArenaSizeChanged(it.toSize()) }
            .onArenaTap(onTap)
            .touchControls(touchState)
            .semantics {
                contentDescription = "Green football field with horizontal yard lines and an orange player. " +
                    "Drag to steer, use WASD or arrow keys, or tap to position the player."
            },
    ) {
        drawFootballField()
        player?.let { drawPlayer(it) }
    }
}

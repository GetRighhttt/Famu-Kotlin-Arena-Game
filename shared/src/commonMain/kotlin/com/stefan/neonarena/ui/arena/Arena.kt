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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.stefan.neonarena.game.model.Player
import com.stefan.neonarena.game.input.onArenaTap
import com.stefan.neonarena.game.rendering.drawPlayer

/**
 * Draws the arena and reports its size and tap positions to the state owner.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
@Composable
fun Arena(
    player: Player?,
    onArenaSizeChanged: (Size) -> Unit,
    onTap: (Offset, Size) -> Unit,
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF090D19))
            .safeDrawingPadding()
            .padding(24.dp)
            .border(1.dp, Color(0xFF27665F))
            .clipToBounds()
            .onSizeChanged { onArenaSizeChanged(it.toSize()) }
            .onArenaTap(onTap)
            .semantics {
                contentDescription = "Arena with a cyan player. Tap to position the player."
            },
    ) {
        drawRect(color = Color(0xFF101B2A))
        player?.let { drawPlayer(it) }
    }
}

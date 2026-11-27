package com.stefan.neonarena.ui.arena

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.text.rememberTextMeasurer
import com.stefan.neonarena.game.state.FieldCamera
import com.stefan.neonarena.game.model.RunStatus
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
import com.stefan.neonarena.game.rendering.drawDefender
import com.stefan.neonarena.game.rendering.drawFootballField
import com.stefan.neonarena.game.rendering.drawPlayer
import com.stefan.neonarena.ui.theme.GameColors

/**
 * Translates world drawings through the camera and overlays a stopped-run result.
 * Field gestures are enabled only during a running round.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
@Composable
fun Arena(
    player: Player?,
    defender: Player?,
    touchState: TouchState,
    isRunning: Boolean,
    status: RunStatus,
    camera: FieldCamera,
    onRestart: () -> Unit,
    onArenaSizeChanged: (Size) -> Unit,
    onTap: (Offset, Size) -> Unit,
    modifier: Modifier = Modifier,
) {
    val textMeasurer = rememberTextMeasurer()
    Column(
        modifier = modifier.fillMaxSize()
            .background(GameColors.background)
            .safeDrawingPadding()
            .padding(horizontal = 12.dp, vertical = 4.dp),
    ) {
        MatchupStrip()
        Box(Modifier.weight(1f)) {
            Canvas(
                modifier = Modifier.fillMaxSize()
                    .border(1.dp, GameColors.rail)
                    .clipToBounds()
                    .onSizeChanged { onArenaSizeChanged(it.toSize()) }
                    .then(if (isRunning) Modifier.onArenaTap(onTap).touchControls(touchState) else Modifier)
                    .semantics {
                        contentDescription = "Football field with a FAMU runner and BCU defender. " +
                            when (status) {
                                RunStatus.Tackled -> "Run stopped after a tackle."
                                RunStatus.Touchdown -> "Touchdown. Run completed."
                                RunStatus.Running -> "Drag or use WASD or arrow keys to steer on the scrolling field."
                            }
                    },
            ) {
                translate(top = -camera.offsetY) {
                    drawFootballField(camera.fieldSize, camera.endZoneHeight, textMeasurer)
                    defender?.let { drawDefender(it, player?.position) }
                    player?.let { drawPlayer(it) }
                }
            }
            if (status != RunStatus.Running) {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.65f)).padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    RunResultOverlay(status, onRestart)
                }
            }
        }
        DriveStrip(player, camera)
    }
}

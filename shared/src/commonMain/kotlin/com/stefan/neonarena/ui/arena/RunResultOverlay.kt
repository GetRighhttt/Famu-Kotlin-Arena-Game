package com.stefan.neonarena.ui.arena

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stefan.neonarena.game.model.RunStatus
import com.stefan.neonarena.ui.theme.GameColors

/**
 * Shows the run result and a keyboard-accessible action to restart the run.
 * The parent arena places this card above the stopped field.
 *
 * @author Stefan Bayne
 * @since 2026-11-27
 */
@Composable
fun RunResultOverlay(status: RunStatus, onRestart: () -> Unit) {
    val title: String
    val message: String
    when (status) {
        RunStatus.Running -> return
        RunStatus.Tackled -> { title = "Tackled"; message = "BCU stopped the run." }
        RunStatus.Touchdown -> { title = "Touchdown!"; message = "FAMU reached the end zone." }
    }
    val accent = if (status == RunStatus.Touchdown) GameColors.famuOrange else GameColors.bcuGold
    Column(
        modifier = Modifier.widthIn(max = 320.dp)
            .fillMaxWidth()
            .background(GameColors.background)
            .border(1.dp, GameColors.rail)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(Modifier.fillMaxWidth(0.18f).height(4.dp).background(accent))
        BasicText(title, style = TextStyle(
            color = GameColors.cream, fontSize = 34.sp, fontWeight = FontWeight.Black, fontStyle = FontStyle.Italic,
        ))
        BasicText(message, style = TextStyle(
            color = GameColors.fieldLine, fontSize = 16.sp,
        ))
        Box(
            modifier = Modifier.fillMaxWidth().background(accent)
                .clickable(role = Role.Button, onClick = onRestart)
                .padding(vertical = 14.dp),
            contentAlignment = Alignment.Center,
        ) {
            BasicText("Try again", style = TextStyle(
                color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold,
            ))
        }
    }
}

package com.github.freetube.ui.global.player

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun MiniPlayer(
    modifier: Modifier = Modifier,
    screenModel: PlayerScreenModel,
    showBottomSheet: () -> Unit,
) {
    
    Row (
        modifier = modifier
            .padding(bottom = 12.dp)
            .padding(horizontal = 20.dp)
            .widthIn(max = 600.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { showBottomSheet() },
    ) {
        // todo miniPLayer
        Text("miniPlayer")
    }
}

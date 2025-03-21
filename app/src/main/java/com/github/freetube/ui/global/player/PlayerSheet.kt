package com.github.freetube.ui.global.player

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerSheet(
) {
    val viewModel = koinInject<PlayerScreenModel>()
    
    ModalBottomSheet(
        containerColor = Color(0xFF111111),
        modifier = Modifier.fillMaxSize(),
        onDismissRequest = {},
        dragHandle = {
            // todo video player
        }
    ) {
        Text("player sheet contents")
    }
}

package com.github.freetube.ui.global.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.freetube.ui.global.player.components.PlayerView
import com.github.freetube.ui.global.player.components.SheetBody

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerSheet(
    screenModel: PlayerScreenModel,
    toChannelScreen: (String) -> Unit,
    dismissSheet: () -> Unit,
) {
    val uiState by screenModel.state.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        sheetState = sheetState,
        containerColor = Color(0xFF111111),
        contentColor = Color(0xFF111111),
        scrimColor = Color(0xFF111111),
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize(),
        onDismissRequest = dismissSheet,
        shape = RectangleShape,
        dragHandle = {
            if (uiState is PlayerScreenModel.UiState.Success) {
                PlayerView(
                    modifier = Modifier.fillMaxWidth(),
                    player = screenModel.viewPlayer
                )
            }
        },
    ) {
        // content animation based on progress currently not possible 
        when (uiState) {
            is PlayerScreenModel.UiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF111111)),
                    contentAlignment = Alignment.Center,
                ) { CircularProgressIndicator() }
            }
            is PlayerScreenModel.UiState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF111111)),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    (uiState as PlayerScreenModel.UiState.Error).message?.let {
                        Text(it)
                    }
                    Button(
                        modifier = Modifier.padding(8.dp),
                        onClick = {
                            // todo refresh
                        },
                    ) {
                        Text("refresh")
                    }
                }
            }
            is PlayerScreenModel.UiState.Success -> {
                SheetBody(
                    videoData = (uiState as PlayerScreenModel.UiState.Success).data,
                    toChannelScreen = toChannelScreen,
                )
            }
        }
    }
}

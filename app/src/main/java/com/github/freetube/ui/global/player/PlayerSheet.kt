package com.github.freetube.ui.global.player

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch

const val LOADING_SHARED_KEY = "loading"

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun PlayerSheet(
    viewModel: PlayerViewModel,
    toChannelScreen: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentPosition by viewModel.currentPosition.collectAsStateWithLifecycle()
    val playerState by viewModel.playerState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { scope.launch { sheetState.partialExpand() } },
        dragHandle = null,
        sheetState = sheetState,
    ) {
        SharedTransitionLayout {
            AnimatedContent(sheetState.currentValue) { value ->
                when (value) {
                    SheetValue.Hidden -> {}
                    SheetValue.Expanded -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .sharedElement(
                                    state = rememberSharedContentState(LOADING_SHARED_KEY),
                                    animatedVisibilityScope = this,
                                ),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    SheetValue.PartiallyExpanded -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.1f)
                                .clickable { scope.launch { sheetState.expand() } }
                                .sharedElement(
                                    state = rememberSharedContentState(LOADING_SHARED_KEY),
                                    animatedVisibilityScope = this,
                                ),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}

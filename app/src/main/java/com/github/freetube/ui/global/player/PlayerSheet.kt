package com.github.freetube.ui.global.player

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

const val LOADING_SHARED_KEY = "loading"

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun BoxScope.PlayerSheet(
    viewModel: PlayerViewModel,
    toChannelScreen: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentPosition by viewModel.currentPosition.collectAsStateWithLifecycle()
    val playerState by viewModel.playerState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    var bottomSheetHeight: Dp
    var progress: Float = 0f
    val animatedProgress = animateFloatAsState(progress)

    LaunchedEffect(1) {
        sheetState.expand()
        bottomSheetHeight = screenHeight - sheetState.requireOffset().roundToInt().dp
        progress = (bottomSheetHeight.value / screenHeight.value + 0.37f) * 0.73f
    }

    ModalBottomSheet(
        onDismissRequest = { scope.launch { sheetState.partialExpand() } },
        dragHandle = null,
        sheetState = sheetState,
    ) {

        SharedTransitionLayout(Modifier.fillMaxSize()) {
            AnimatedContent(
                targetState = sheetState.currentValue,

                ) { value ->

                when (value) {
                    SheetValue.Hidden -> {}
                    SheetValue.Expanded -> {

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .sharedElement(
                                    state = rememberSharedContentState(LOADING_SHARED_KEY),
                                    animatedVisibilityScope = this@AnimatedContent,
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
                                .height(60.dp)
                                .clickable { scope.launch { sheetState.expand() } }
                                .sharedElement(
                                    state = rememberSharedContentState(LOADING_SHARED_KEY),
                                    animatedVisibilityScope = this@AnimatedContent,
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

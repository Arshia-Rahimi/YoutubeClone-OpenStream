package com.github.freetube.ui.global.player

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arshia.freetube.R
import com.github.freetube.core.common.compose.onCondition
import com.github.freetube.ui.global.player.components.PlayerSheetState
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import kotlin.math.roundToInt

const val MINI_PLAYER_WIDTH_RATIO = 0.25f
const val MINI_PLAYER_HEIGHT_RATIO = 0.8f

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PlayerSheet(
    toChannelScreen: (String) -> Unit,
) {
    val viewModel = koinViewModel<PlayerViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentPosition by viewModel.currentPosition.collectAsStateWithLifecycle()
    val playerState by viewModel.playerState.collectAsStateWithLifecycle()
    val density = LocalDensity.current
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp.dp
    val screenHeight = config.screenHeightDp.dp
    val scope = rememberCoroutineScope()
    val dragState = remember {
        AnchoredDraggableState(
            initialValue = PlayerSheetState.MINI_PLAYER,
            anchors = DraggableAnchors {
                with(density) {
                    PlayerSheetState.MINI_PLAYER at
                            (screenHeight.value * MINI_PLAYER_HEIGHT_RATIO).dp.toPx()
                    PlayerSheetState.FULL_SCREEN at 0.dp.toPx()
                }
            },
            positionalThreshold = { distance: Float -> distance * 0.01f },
            velocityThreshold = { 100f },
            decayAnimationSpec = exponentialDecay(),
            snapAnimationSpec = spring(
                stiffness = Spring.StiffnessLow
            ),
        )
    }
    var sheetHeight by remember { mutableFloatStateOf(0f) }
    val progress = (-(1 - MINI_PLAYER_WIDTH_RATIO) / (screenHeight.value * 0.8f) * dragState.offset) + 1
    val playerWidth = progress * screenWidth.value
    val animatedWidth by animateFloatAsState(playerWidth)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .offset {
                IntOffset(
                    x = 0,
                    y = dragState.requireOffset().roundToInt(),
                )
            }
            .background(Color.Transparent)
            .anchoredDraggable(
                state = dragState,
                orientation = Orientation.Vertical,
                enabled = dragState.settledValue == PlayerSheetState.FULL_SCREEN,
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray)
                .onGloballyPositioned {
                    sheetHeight = it.boundsInRoot().top
                }
                .onCondition(dragState.currentValue == PlayerSheetState.MINI_PLAYER) {
                    clickable {
                        scope.launch { dragState.animateTo(PlayerSheetState.FULL_SCREEN) }
                    }
                },
            horizontalArrangement = Arrangement.Start,
        ) {
            Box(
                modifier = Modifier
                    .systemBarsPadding()
                    .width(animatedWidth.dp)
                    .aspectRatio(16 / 9f),
            ) {
                Icon(
                    modifier = Modifier.matchParentSize(),
                    painter = painterResource(R.drawable.subs_selected),
                    contentDescription = null,
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(MaterialTheme.colorScheme.background),
        ) {
            // todo body
        }
    }
}

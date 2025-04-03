package com.github.freetube.ui.global.player

import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionOnScreen
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arshia.freetube.R
import com.github.freetube.core.common.util.onCondition
import com.github.freetube.ui.global.player.components.PlayerSheetState
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PlayerSheet(
    viewModel: PlayerViewModel,
    toChannelScreen: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentPosition by viewModel.currentPosition.collectAsStateWithLifecycle()
    val playerState by viewModel.playerState.collectAsStateWithLifecycle()
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    val dragState = remember {
        AnchoredDraggableState(
            initialValue = PlayerSheetState.MINI_PLAYER,
            positionalThreshold = { distance: Float -> distance * 0.1f },
            velocityThreshold = { with(density) { 100f } },
            snapAnimationSpec = tween(),
            decayAnimationSpec = exponentialDecay(),
        ).apply {
            updateAnchors(
                DraggableAnchors {
                    PlayerSheetState.MINI_PLAYER at 0.1f
                    PlayerSheetState.FULL_SCREEN at 1f
                }
            )
        }
    }
    var sheetState by remember { mutableStateOf(PlayerSheetState.MINI_PLAYER) }
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp.dp
    val screenHeight = config.screenHeightDp.dp
    val slightlyExpandedPlayerWidth = screenWidth * 0.2f
    var sheetHeight by remember { mutableFloatStateOf(slightlyExpandedPlayerWidth.value) }
    val playerWidth by remember {
        derivedStateOf {
//            when (sheetState.currentValue) {
//                FlexibleSheetValue.SlightlyExpanded -> slightlyExpandedPlayerWidth
//                else -> sheetHeight / screenHeight
//            }
        }
    }
//    val animatedPlayerWidth by animateFloatAsState(playerWidth)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .offset {
                IntOffset(
                    y = 0,
                    x = -dragState.requireOffset().roundToInt(),
                )
            }
            .background(Color.Transparent)
            .anchoredDraggable(
                state = dragState,
                orientation = Orientation.Vertical,
                enabled = sheetState == PlayerSheetState.FULL_SCREEN,
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray)
                .onGloballyPositioned {
                    sheetHeight = it.positionOnScreen().y.also(::println)
                }
                .onCondition(sheetState == PlayerSheetState.MINI_PLAYER) {
                    clickable {
                        sheetState = PlayerSheetState.FULL_SCREEN
                    }
                },
            horizontalArrangement = Arrangement.Start,
        ) {
            Box(
                modifier = Modifier
                    .systemBarsPadding()
//                        .width(animatedPlayerWidth.dp)
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

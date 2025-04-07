package com.github.freetube.ui.global.player

import androidx.compose.animation.core.Spring
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arshia.freetube.R
import com.github.freetube.core.common.compose.onCondition
import com.github.freetube.ui.global.player.components.PlayerSheetState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import kotlin.math.roundToInt

const val MINI_PLAYER_WIDTH_RATIO = 0.25f

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayerSheet(
    navBarOffset: Float,
    toChannelScreen: (String) -> Unit,
) {
    val viewModel = koinViewModel<PlayerViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentPosition by viewModel.currentPosition.collectAsStateWithLifecycle()
    val playerState by viewModel.playerState.collectAsStateWithLifecycle()
    val density = LocalDensity.current
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp.dp
    val scope = rememberCoroutineScope()
    val miniPlayerOffset = navBarOffset - with(density) { (screenWidth.toPx() * 9 / 64) }
    val dragState = remember {
        AnchoredDraggableState(
            initialValue = PlayerSheetState.MINI_PLAYER,
            anchors = DraggableAnchors {
                PlayerSheetState.MINI_PLAYER at miniPlayerOffset
                PlayerSheetState.FULL_SCREEN at 0f
            },
            positionalThreshold = { distance: Float -> distance * 0.01f },
            velocityThreshold = { 100f },
            decayAnimationSpec = exponentialDecay(),
            snapAnimationSpec = spring(
                stiffness = Spring.StiffnessLow
            ),
        )
    }
    LaunchedEffect(miniPlayerOffset) {
        dragState.updateAnchors(DraggableAnchors {
            PlayerSheetState.MINI_PLAYER at miniPlayerOffset
            PlayerSheetState.FULL_SCREEN at 0f
        })
    }
    val sheetDragProgress = (-dragState.offset / miniPlayerOffset) + 1
    val playerWidth =
        ((1 - MINI_PLAYER_WIDTH_RATIO) * sheetDragProgress + MINI_PLAYER_WIDTH_RATIO) * screenWidth.value

    PlayerSheet(
        dragState = dragState,
        scope = scope,
        playerWidth = playerWidth,
        sheetDragProgress = sheetDragProgress,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PlayerSheet(
    dragState: AnchoredDraggableState<PlayerSheetState>,
    scope: CoroutineScope,
    playerWidth: Float,
    sheetDragProgress: Float,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
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
                    .width(playerWidth.dp)
                    .aspectRatio(16 / 9f),
            ) {
                Icon(
                    modifier = Modifier.matchParentSize(),
                    painter = painterResource(R.drawable.subs_selected),
                    contentDescription = null,
                )
            }
        }
        if (sheetDragProgress != 0f) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .alpha(sheetDragProgress)
                    .background(MaterialTheme.colorScheme.background),
            ) {
                // todo body
            }
        }
    }
}

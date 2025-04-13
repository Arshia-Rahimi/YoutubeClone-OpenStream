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
import androidx.compose.foundation.gestures.snapTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.Player
import com.arshia.freetube.R
import com.github.freetube.core.common.compose.onCondition
import com.github.freetube.core.media3.PlayerState
import com.github.freetube.ui.global.player.components.PlayerSheetState
import com.github.freetube.ui.global.player.components.PlayerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import kotlin.math.roundToInt

const val MINI_PLAYER_WIDTH_TO_SCREEN_WIDTH_RATIO = 0.3f
const val MINI_PLAYER_CONTENT_VISIBILITY_THRESHOLD = 0.75f
const val VIDEO_PROGRESS_INDICATOR_THICKNESS = 2

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayerSheet(
    navBarOffset: Float,
    toChannelScreen: (String) -> Unit,
) {
    val viewModel = koinViewModel<PlayerViewModel>()
    val showMiniPlayer by viewModel.showMiniPlayer.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentPosition by viewModel.currentPosition.collectAsStateWithLifecycle()
    val playerState by viewModel.playerState.collectAsStateWithLifecycle()
    val density = LocalDensity.current
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp.dp
    val miniPlayerHeight = with(density) { (screenWidth * 9 / 64).toPx() }
    val statusBarPadding = WindowInsets.statusBars.getTop(density).toFloat()
    val miniPlayerOffset =
        navBarOffset - miniPlayerHeight - statusBarPadding - with(density) { VIDEO_PROGRESS_INDICATOR_THICKNESS.dp.toPx() }
    val dragState = remember {
        AnchoredDraggableState(
            initialValue = PlayerSheetState.MINI_PLAYER,
            anchors = DraggableAnchors {
                PlayerSheetState.MINI_PLAYER at miniPlayerOffset
                PlayerSheetState.EXPANDED at 0f
            },
            positionalThreshold = { distance: Float -> distance * 0.01f },
            velocityThreshold = { 100f },
            decayAnimationSpec = exponentialDecay(),
            snapAnimationSpec = spring(
                stiffness = Spring.StiffnessLow
            ),
        )
    }
    val sheetDragProgress = (-dragState.offset / miniPlayerOffset) + 1
    val playerWidth =
        ((1 - MINI_PLAYER_WIDTH_TO_SCREEN_WIDTH_RATIO) * sheetDragProgress + MINI_PLAYER_WIDTH_TO_SCREEN_WIDTH_RATIO) * screenWidth.value

    LaunchedEffect(miniPlayerOffset) {
        dragState.updateAnchors(DraggableAnchors {
            PlayerSheetState.MINI_PLAYER at miniPlayerOffset
            PlayerSheetState.EXPANDED at 0f
        })
        dragState.snapTo(PlayerSheetState.MINI_PLAYER)
    }

    if (showMiniPlayer) {
        val player = viewModel.viewPlayer
        PlayerSheet(
            player = player,
            dragState = dragState,
            playerWidth = playerWidth,
            sheetDragProgress = sheetDragProgress,
            toChannelScreen = toChannelScreen,
            currentPosition = currentPosition,
            uiState = uiState,
            playerState = playerState,
            dispose = { viewModel.dispose() },
            togglePlay = { viewModel.togglePlay() },
            isSheetExpanded = dragState.settledValue == PlayerSheetState.EXPANDED,
            screenWidth = screenWidth,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PlayerSheet(
    player: Player,
    screenWidth: Dp,
    dragState: AnchoredDraggableState<PlayerSheetState>,
    playerWidth: Float,
    sheetDragProgress: Float,
    currentPosition: Long,
    uiState: PlayerViewModel.UiState,
    playerState: PlayerState,
    isSheetExpanded: Boolean,
    scope: CoroutineScope = rememberCoroutineScope(),
    toChannelScreen: (String) -> Unit,
    dispose: () -> Unit,
    togglePlay: () -> Unit,
) {
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxWidth()
            .offset {
                IntOffset(
                    x = 0,
                    y = dragState.requireOffset().roundToInt(),
                )
            }
            .anchoredDraggable(
                state = dragState,
                orientation = Orientation.Vertical,
            ),
    ) {
        val miniPlayerContentAlpha =
            (-sheetDragProgress / MINI_PLAYER_CONTENT_VISIBILITY_THRESHOLD) + 1f
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .onCondition(sheetDragProgress < MINI_PLAYER_CONTENT_VISIBILITY_THRESHOLD) {
                        background(
                            MaterialTheme.colorScheme.tertiaryContainer.copy(
                                miniPlayerContentAlpha
                            )
                        )
                    }
                    .onCondition(dragState.currentValue == PlayerSheetState.MINI_PLAYER) {
                        clickable {
                            scope.launch { dragState.animateTo(PlayerSheetState.EXPANDED) }
                        }
                    },
                horizontalArrangement = Arrangement.spacedBy(
                    space = 4.dp,
                    alignment = Alignment.Start,
                ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .width(playerWidth.dp)
                        .aspectRatio(16 / 9f)
                        .background(MaterialTheme.colorScheme.tertiaryContainer),
                    contentAlignment = Alignment.Center,
                ) {
                    when (uiState) {
                        is PlayerViewModel.UiState.Success -> {
                            PlayerView(
                                player = player,
                                modifier = Modifier.matchParentSize(),
                                isSheetExpanded = isSheetExpanded,
                            )
                        }

                        is PlayerViewModel.UiState.Loading -> LinearProgressIndicator()
                        else -> Unit
                    }
                }

                if (sheetDragProgress < MINI_PLAYER_CONTENT_VISIBILITY_THRESHOLD) {
                    Text(
                        text = "title",
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .weight(1f)
                            .alpha(miniPlayerContentAlpha),
                    )
                    IconButton(
                        onClick = { togglePlay() },
                        enabled = sheetDragProgress == 0f,
                        modifier = Modifier.alpha(miniPlayerContentAlpha),
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.play),
                            contentDescription = "toggle play",
                            tint = Color.White,
                        )
                    }
                    IconButton(
                        onClick = { dispose() },
                        enabled = sheetDragProgress == 0f,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .alpha(miniPlayerContentAlpha),
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.cross),
                            contentDescription = "dispose player",
                            tint = Color.White,
                        )
                    }
                }
            }
        }
        if (uiState is PlayerViewModel.UiState.Success) {
            if (sheetDragProgress < MINI_PLAYER_CONTENT_VISIBILITY_THRESHOLD) {
//        var progress by remember { mutableFloatStateOf(0f) }
//        LaunchedEffect(currentPosition) {
//            progress = currentPosition.toFloat() / video.length.toFloat()
//        }
//        val animatedProgress by animateFloatAsState(
//            targetValue = progress,
//            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
//        )
                LinearProgressIndicator(
                    drawStopIndicator = {},
                    gapSize = 0.dp,
                    strokeCap = StrokeCap.Square,
                    trackColor = Color(0xFF5D5D5D),
                    color = Color(0xFFBBBBBB),
                    progress = { 0.5f },
                    modifier = Modifier
                        .height(VIDEO_PROGRESS_INDICATOR_THICKNESS.dp)
                        .fillMaxWidth()
                        .alpha(miniPlayerContentAlpha),
                )
            }
        }
        if (sheetDragProgress != 0f) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .alpha(sheetDragProgress)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                // todo body
            }
        }
    }
}

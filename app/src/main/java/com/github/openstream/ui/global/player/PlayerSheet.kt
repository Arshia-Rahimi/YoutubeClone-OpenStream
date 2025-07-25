package com.github.openstream.ui.global.player

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableDefaults
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.Player
import com.github.openstream.R
import com.github.openstream.core.common.compose.onCondition
import com.github.openstream.core.common.compose.safeOffset
import com.github.openstream.core.common.util.toTime
import com.github.openstream.core.media3.OpenStreamMediaPlayer
import com.github.openstream.core.shared.MiniPlayerConfig
import com.github.openstream.core.shared.dataitem.ChannelItem
import com.github.openstream.core.shared.dataitem.VideoItem
import com.github.openstream.core.shared.extractor.data.VideoData
import com.github.openstream.core.shared.extractor.data.VideoOption
import com.github.openstream.core.shared.extractor.data.VideoQuality
import com.github.openstream.ui.global.player.components.SheetBodyPager
import com.github.openstream.ui.global.player.components.playerview.PlayerView
import com.github.openstream.ui.global.player.model.PlayerSheetState
import com.github.openstream.ui.global.player.model.VideoLocalState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import kotlin.math.roundToInt

// migrate deprecated screenWidth -boxWithConstraints in recommended
@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayerSheet(
    navBarOffset: Float,
    toChannelScreen: (String) -> Unit,
    toPlaylistScreen: (String) -> Unit,
) {
    val viewModel = koinViewModel<PlayerViewModel>()
    
    val sheetState by viewModel.sheetState.collectAsStateWithLifecycle()
    val showMiniPlayer by viewModel.showMiniPlayer.collectAsStateWithLifecycle()
    val fetchingState by viewModel.fetchingState.collectAsStateWithLifecycle()
    val currentPosition by viewModel.currentPosition.collectAsStateWithLifecycle()
    val currentQuality by viewModel.currentQuality.collectAsStateWithLifecycle()
    val videoLocalState by viewModel.videoLocalState.collectAsStateWithLifecycle()
    val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()
    val isAudioOnlyModeEnabled by viewModel.isAudioOnlyModeEnabled.collectAsStateWithLifecycle()
    val currentVideoData by viewModel.currentVideoData.collectAsStateWithLifecycle()
    val currentVideo by viewModel.currentVideo.collectAsStateWithLifecycle()
    val orientation by viewModel.orientation.collectAsStateWithLifecycle()
    
    val localConfig = LocalConfiguration.current
    val density = LocalDensity.current
    val config = LocalConfiguration.current
    
    val orientationC = localConfig.orientation
    val screenWidth = config.screenWidthDp.dp
    val widthToScreenWidthRatio = if(orientationC == Configuration.ORIENTATION_LANDSCAPE)
        MiniPlayerConfig.LANDSCAPE_WIDTH_TO_SCREEN_WIDTH_RATIO else MiniPlayerConfig.WIDTH_TO_SCREEN_WIDTH_RATIO
    
    val miniPlayerHeight =
        with(density) { (screenWidth * widthToScreenWidthRatio * 9 / 16).toPx() }
    val statusBarPadding = WindowInsets.statusBars.getTop(density).toFloat()
    val miniPlayerOffset = navBarOffset - miniPlayerHeight - statusBarPadding
    
    val dragState = rememberSaveable(
        saver = AnchoredDraggableState.Saver(),
    ) { AnchoredDraggableState(PlayerSheetState.MINI_PLAYER) }
    
    val sheetDragProgress = (-dragState.offset / miniPlayerOffset) + 1
    val playerWidth =
        ((1 - widthToScreenWidthRatio) * sheetDragProgress + widthToScreenWidthRatio) *
                screenWidth.value
    
    val scope = rememberCoroutineScope()

    LaunchedEffect(dragState) {
        snapshotFlow { dragState.currentValue }
            .distinctUntilChanged()
            .collect { viewModel.updateSheetState(it) }
    }

    LaunchedEffect(orientationC) {
        (if (orientationC == Configuration.ORIENTATION_LANDSCAPE)
            com.github.openstream.core.common.compose.Orientation.LandScape
        else com.github.openstream.core.common.compose.Orientation.Portrait)
            .let { viewModel.onOrientationChanged(it) }
    }

    LaunchedEffect(miniPlayerOffset) {
        dragState.updateAnchors(DraggableAnchors {
            PlayerSheetState.MINI_PLAYER at miniPlayerOffset
            PlayerSheetState.EXPANDED at 0f
        })
        // miniPlayerOffset is negative before composition
        if (miniPlayerOffset > 0 && !viewModel.isInitialSnapDone.value) {
            dragState.snapTo(PlayerSheetState.MINI_PLAYER)
            viewModel.isInitialSnapDone.value = true
        }
    }

    if (showMiniPlayer) {
        PlayerSheet(
            subscribe = viewModel::subscribe,
            density = density,
            screenWidth = screenWidth,
            queue = viewModel.queue,
            player = viewModel.playerInstance,
            dragState = dragState,
            collapseMiniPlayer = { scope.launch { dragState.animateTo(PlayerSheetState.MINI_PLAYER) } },
            scope = scope,
            playerWidth = playerWidth,
            isFullScreen = orientation == com.github.openstream.core.common.compose.Orientation.LandScape,
            currentQuality = currentQuality?.quality,
            sheetDragProgress = sheetDragProgress,
            toChannelScreen = toChannelScreen,
            currentPosition = currentPosition,
            sheetState = sheetState,
            toPlaylistScreen = toPlaylistScreen,
            fetchingState = fetchingState,
            isPlaying = isPlaying,
            dispose = viewModel::dispose,
            isSheetExpanded = dragState.settledValue == PlayerSheetState.EXPANDED,
            toggleVideoLiked = viewModel::toggleVideoLiked,
            toggleVideoWatchLater = viewModel::toggleVideoWatchLater,
            videoLocalState = videoLocalState,
            currentVideoData = currentVideoData,
            currentVideo = currentVideo,
            switchPlaybackQuality = viewModel::switchPlaybackQuality,
            isAudioOnlyModeEnabled = isAudioOnlyModeEnabled,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PlayerSheet(
    player: Player,
    dragState: AnchoredDraggableState<PlayerSheetState>,
    isFullScreen: Boolean,
    sheetState: PlayerSheetState,
    playerWidth: Float,
    density: Density,
    screenWidth: Dp,
    sheetDragProgress: Float,
    currentPosition: Long,
    fetchingState: OpenStreamMediaPlayer.FetchingState,
    queue: List<VideoItem>,
    isPlaying: Boolean,
    isSheetExpanded: Boolean,
    currentQuality: VideoQuality?,
    currentVideoData: VideoData?,
    currentVideo: VideoItem?,
    videoLocalState: VideoLocalState,
    isAudioOnlyModeEnabled: Boolean,
    scope: CoroutineScope,
    toChannelScreen: (String) -> Unit,
    dispose: () -> Unit,
    toggleVideoWatchLater: () -> Unit,
    toggleVideoLiked: () -> Unit,
    switchPlaybackQuality: (VideoOption) -> Unit,
    subscribe: (ChannelItem.OnlineChannelItem) -> Unit,
    collapseMiniPlayer: () -> Unit,
    toPlaylistScreen: (String) -> Unit,
) {
    val xOffset = with(density) {
        if(screenWidth <= 600.dp) 0
        else ((screenWidth - 600.dp).toPx() / 2).roundToInt()
    }
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .widthIn(max = 600.dp)
            .fillMaxWidth()
            .offset {
                IntOffset(
                    x = xOffset,
                    y = dragState.safeOffset().roundToInt(),
                )
            }
            .anchoredDraggable(
                state = dragState,
                orientation = Orientation.Vertical,
                flingBehavior = AnchoredDraggableDefaults.flingBehavior(
                    positionalThreshold = { distance: Float -> distance * 0.01f },
                    state = dragState,
                )
            ),
    ) {
        val miniPlayerContentAlpha =
            (-sheetDragProgress / MiniPlayerConfig.CONTENT_VISIBILITY_THRESHOLD) + 1f
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .onCondition(sheetDragProgress < MiniPlayerConfig.CONTENT_VISIBILITY_THRESHOLD) {
                        background(
                            MaterialTheme.colorScheme.tertiaryContainer.copy(
                                miniPlayerContentAlpha
                            )
                        )
                    }
                    .onCondition(sheetState == PlayerSheetState.MINI_PLAYER) {
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
                    when (fetchingState) {
                        is OpenStreamMediaPlayer.FetchingState.Success -> {
                            PlayerView(
                                isFullScreen = isFullScreen,
                                player = player,
                                modifier = Modifier.matchParentSize(),
                                isSheetExpanded = isSheetExpanded,
                                isAudioModeEnabled = isAudioOnlyModeEnabled,
                                videoThumbnail = currentVideo?.thumbnail ?: "",
                            )
                        }

                        is OpenStreamMediaPlayer.FetchingState.Loading -> CircularProgressIndicator()
                        is OpenStreamMediaPlayer.FetchingState.Error -> {
                            Icon(
                                painter = painterResource(R.drawable.cross),
                                contentDescription = "",
                                tint = Color.White,
                            )
                        }
                    }
                }

                if (sheetDragProgress < MiniPlayerConfig.CONTENT_VISIBILITY_THRESHOLD) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                            .alpha(miniPlayerContentAlpha),
                        verticalArrangement = Arrangement.SpaceBetween,
                    ) {
                        if (fetchingState is OpenStreamMediaPlayer.FetchingState.Success) {
                            Text(
                                text = currentVideoData?.name ?: "",
                                color = MaterialTheme.colorScheme.onPrimary,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                            )
                            Text(
                                text = currentPosition.toTime() + " / " + currentVideoData?.duration?.toTime(),
                                color = MaterialTheme.colorScheme.onBackground,
                                maxLines = 1,
                            )
                        }
                    }
                    if (fetchingState is OpenStreamMediaPlayer.FetchingState.Success) {
                        IconButton(
                            onClick = PlayerAction.TogglePlay::send,
                            enabled = sheetDragProgress == 0f,
                            modifier = Modifier.alpha(miniPlayerContentAlpha),
                        ) {
                            Icon(
                                painter = painterResource(if (isPlaying) R.drawable.pause else R.drawable.play),
                                contentDescription = "toggle play",
                                tint = Color.White,
                            )
                        }
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

        SheetBodyPager(
            sheetDragProgress = sheetDragProgress,
            toPlaylistScreen = toPlaylistScreen,
            isAudioOnlyModeEnabled = isAudioOnlyModeEnabled,
            currentQuality = currentQuality,
            switchPlaybackQuality = switchPlaybackQuality,
            fetchingState = fetchingState,
            currentVideoData = currentVideoData,
            scope = scope,
            toChannelScreen = toChannelScreen,
            videoLocalState = videoLocalState,
            toggleVideoLiked = toggleVideoLiked,
            toggleVideoWatchLater = toggleVideoWatchLater,
            queue = queue,
            isPlaying = isPlaying,
            currentPosition = currentPosition,
            currentVideo = currentVideo,
            subscribe = subscribe,
            collapseMiniPlayer = collapseMiniPlayer,
        )
    }
}

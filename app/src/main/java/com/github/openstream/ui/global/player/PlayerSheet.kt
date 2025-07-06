package com.github.openstream.ui.global.player

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.Player
import coil3.compose.AsyncImage
import com.github.openstream.R
import com.github.openstream.core.common.compose.PainterIconButton
import com.github.openstream.core.common.compose.onCondition
import com.github.openstream.core.common.util.toTime
import com.github.openstream.core.media3.OpenStreamMediaPlayer
import com.github.openstream.core.model.dataitem.VideoItem
import com.github.openstream.core.model.extractordata.VideoData
import com.github.openstream.core.shared.MINI_PLAYER_CONTENT_VISIBILITY_THRESHOLD
import com.github.openstream.core.shared.MINI_PLAYER_WIDTH_TO_SCREEN_WIDTH_RATIO
import com.github.openstream.core.shared.VIDEO_PROGRESS_INDICATOR_THICKNESS
import com.github.openstream.ui.global.player.components.PlayerSheetState
import com.github.openstream.ui.global.player.components.SheetBody
import com.github.openstream.ui.global.player.components.SheetBodyPage
import com.github.openstream.ui.global.player.components.VideoPlaylistsState
import com.github.openstream.ui.global.player.view.PlayerView
import kotlinx.coroutines.CoroutineScope
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
) {
    val viewModel = koinViewModel<PlayerViewModel>()
    val showMiniPlayer by viewModel.showMiniPlayer.collectAsStateWithLifecycle()
    val fetchingState by viewModel.fetchingState.collectAsStateWithLifecycle()
    val currentPosition by viewModel.currentPosition.collectAsStateWithLifecycle()
    val playlistsState by viewModel.playlistsState.collectAsStateWithLifecycle()
    val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()
    val playbackSpeed by viewModel.playbackSpeed.collectAsStateWithLifecycle()
    val currentVideoData by viewModel.currentVideoData.collectAsStateWithLifecycle()
    val currentVideo by viewModel.currentVideo.collectAsStateWithLifecycle()
    val density = LocalDensity.current
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp.dp
    val miniPlayerHeight =
        with(density) { (screenWidth * MINI_PLAYER_WIDTH_TO_SCREEN_WIDTH_RATIO * 9 / 16).toPx() }
    val statusBarPadding = WindowInsets.statusBars.getTop(density).toFloat()
    val miniPlayerOffset =
        navBarOffset - miniPlayerHeight - statusBarPadding - with(density) { VIDEO_PROGRESS_INDICATOR_THICKNESS.dp.toPx() }
    val dragState = viewModel.dragState
    val sheetDragProgress = (-dragState.offset / miniPlayerOffset) + 1
    val playerWidth =
        ((1 - MINI_PLAYER_WIDTH_TO_SCREEN_WIDTH_RATIO) * sheetDragProgress + MINI_PLAYER_WIDTH_TO_SCREEN_WIDTH_RATIO) *
                screenWidth.value

    LaunchedEffect(miniPlayerOffset) {
        dragState.updateAnchors(DraggableAnchors {
            PlayerSheetState.MINI_PLAYER at miniPlayerOffset
            PlayerSheetState.EXPANDED at 0f
        })
        dragState.snapTo(PlayerSheetState.MINI_PLAYER)
    }

    if (showMiniPlayer) {
        PlayerSheet(
            queue = viewModel.queue,
            player = viewModel.playerInstance,
            dragState = dragState,
            playerWidth = playerWidth,
            sheetDragProgress = sheetDragProgress,
            toChannelScreen = toChannelScreen,
            currentPosition = currentPosition,
            fetchingState = fetchingState,
            isPlaying = isPlaying,
            dispose = { viewModel.dispose() },
            isSheetExpanded = dragState.settledValue == PlayerSheetState.EXPANDED,
            toggleVideoLiked = viewModel::toggleVideoLiked,
            toggleVideoWatchLater = viewModel::toggleVideoWatchLater,
            videoPlaylistsState = playlistsState,
            currentVideoData = currentVideoData,
            playbackSpeed = playbackSpeed,
            currentVideo = currentVideo,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PlayerSheet(
    player: Player,
    dragState: AnchoredDraggableState<PlayerSheetState>,
    playerWidth: Float,
    sheetDragProgress: Float,
    currentPosition: Long,
    fetchingState: OpenStreamMediaPlayer.FetchingState,
    queue: List<VideoItem>,
    playbackSpeed: Float,
    isPlaying: Boolean,
    isSheetExpanded: Boolean,
    currentVideoData: VideoData?,
    currentVideo: VideoItem?,
    videoPlaylistsState: VideoPlaylistsState,
    scope: CoroutineScope = rememberCoroutineScope(),
    toChannelScreen: (String) -> Unit,
    dispose: () -> Unit,
    toggleVideoWatchLater: () -> Unit,
    toggleVideoLiked: () -> Unit,
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
                flingBehavior = AnchoredDraggableDefaults.flingBehavior(
                    positionalThreshold = { distance: Float -> distance * 0.01f },
                    state = dragState,
                )
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
                    when (fetchingState) {
                        is OpenStreamMediaPlayer.FetchingState.Success -> {
                            PlayerView(
                                player = player,
                                modifier = Modifier.matchParentSize(),
                                isSheetExpanded = isSheetExpanded,
                            )
                        }

                        is OpenStreamMediaPlayer.FetchingState.Loading -> CircularProgressIndicator()
                        is OpenStreamMediaPlayer.FetchingState.Error -> {}
                    }
                }

                if (sheetDragProgress < MINI_PLAYER_CONTENT_VISIBILITY_THRESHOLD) {
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
        var progress by remember { mutableFloatStateOf(0f) }
        val animatedProgress by animateFloatAsState(
            targetValue = progress,
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        )
        LaunchedEffect(currentPosition) {
            progress = if (fetchingState is OpenStreamMediaPlayer.FetchingState.Success) {
                currentPosition.toFloat() / (currentVideoData?.duration?.toFloat() ?: 1f)
            } else 0f
        }

        LinearProgressIndicator(
            drawStopIndicator = {},
            gapSize = 0.dp,
            strokeCap = StrokeCap.Square,
            trackColor = Color(0xFF5D5D5D),
            color = Color(0xFFBBBBBB),
            progress = { animatedProgress },
            modifier = Modifier
                .height(VIDEO_PROGRESS_INDICATOR_THICKNESS.dp)
                .fillMaxWidth(),
        )

        SheetBodyPager(
            sheetDragProgress = sheetDragProgress,
            fetchingState = fetchingState,
            currentVideoData = currentVideoData,
            scope = scope,
            toChannelScreen = toChannelScreen,
            videoPlaylistsState = videoPlaylistsState,
            toggleVideoLiked = toggleVideoLiked,
            toggleVideoWatchLater = toggleVideoWatchLater,
            queue = queue,
            playbackSpeed = playbackSpeed,
            isPlaying = isPlaying,
            currentPosition = currentPosition,
            currentVideo = currentVideo,
        )
    }
}

@Composable
private fun SheetBodyPager(
    queue: List<VideoItem>,
    playbackSpeed: Float,
    isPlaying: Boolean,
    sheetDragProgress: Float,
    fetchingState: OpenStreamMediaPlayer.FetchingState,
    currentVideoData: VideoData?,
    currentVideo: VideoItem?,
    currentPosition: Long,
    scope: CoroutineScope,
    videoPlaylistsState: VideoPlaylistsState,
    toChannelScreen: (String) -> Unit,
    toggleVideoWatchLater: () -> Unit,
    toggleVideoLiked: () -> Unit,
) {
    val pagerState = rememberPagerState { SheetBodyPage.entries.size }
    if (sheetDragProgress != 0f) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .alpha(sheetDragProgress)
                .background(MaterialTheme.colorScheme.background)
                .navigationBarsPadding()
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) { currentTab ->
                when (currentTab) {
                    SheetBodyPage.VideoDescription.ordinal -> {
                        if (fetchingState is OpenStreamMediaPlayer.FetchingState.Success) {
                            currentVideoData?.let { currentVideo ->
                                SheetBody(
                                    modifier = Modifier.fillMaxSize(),
                                    videoData = currentVideo,
                                    scrollState = rememberScrollState(),
                                    scope = scope,
                                    toChannelScreen = toChannelScreen,
                                    shareVideo = {},
                                    likeVideo = { toggleVideoLiked() },
                                    addToWatchLater = { toggleVideoWatchLater() },
                                    videoPlaylistsState = videoPlaylistsState,
                                )
                            }
                        }
                    }

                    SheetBodyPage.Queue.ordinal -> QueuePage(
                        isPlaying = isPlaying,
                        queue = queue,
                        playbackSpeed = playbackSpeed,
                        currentVideo = currentVideo,
                        currentPosition = currentPosition,
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                SheetBodyPage.entries.forEach { tab ->
                    IconButton(
                        onClick = { scope.launch { pagerState.scrollToPage(tab.ordinal) } },
                        modifier = Modifier.weight(0.5f),
                    ) {
                        Icon(
                            painter = painterResource(if (tab.ordinal == pagerState.currentPage) tab.selectedIcon else tab.icon),
                            contentDescription = null,
                            tint = Color.Unspecified,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QueuePage(
    isPlaying: Boolean,
    queue: List<VideoItem>,
    playbackSpeed: Float,
    currentPosition: Long,
    currentVideo: VideoItem?,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 12.dp),
        ) {
            items(queue) { video ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clickable { PlayerAction.PlayFromItem(video).send() }
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                        .onCondition(video == currentVideo) { background(Color.Gray) },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AsyncImage(
                        model = video.thumbnail,
                        contentDescription = "thumbnail",
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(16 / 9f)
                    )
                    Text(
                        text = video.name,
                        fontSize = 12.sp,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                            .basicMarquee(),
                    )
                    Text(
                        text = video.duration.toTime(),
                        fontSize = 12.sp,
                        color = Color.White,
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            Slider(
                value = currentPosition.toFloat(),
                onValueChange = { position: Float ->
                    println(position)
                    PlayerAction.SeekTo(position.toLong() * 1000).send()
                },
                valueRange = 0f..(currentVideo?.duration?.toFloat() ?: 1f),
                modifier = Modifier
                    .height(VIDEO_PROGRESS_INDICATOR_THICKNESS.dp)
                    .fillMaxWidth(),
                colors = SliderDefaults.colors(
                    // todo
                ),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                PainterIconButton(
                    onClick = { PlayerAction.Previous.send() },
                    drawableRes = R.drawable.skip_previous,
                    tint = Color.White,
                )
                PainterIconButton(
                    onClick = { PlayerAction.TogglePlay.send() },
                    drawableRes = if (isPlaying) R.drawable.pause else R.drawable.play,
                    tint = Color.White,
                )
                PainterIconButton(
                    onClick = { PlayerAction.Next.send() },
                    drawableRes = R.drawable.skip_next,
                    tint = Color.White,
                )
            }
        }
    }
}

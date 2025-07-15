package com.github.openstream.ui.global.player.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.github.openstream.core.media3.OpenStreamMediaPlayer
import com.github.openstream.core.shared.dataitem.ChannelItem
import com.github.openstream.core.shared.dataitem.VideoItem
import com.github.openstream.core.shared.extractor.data.VideoData
import com.github.openstream.core.shared.extractor.data.VideoOption
import com.github.openstream.core.shared.extractor.data.VideoQuality
import com.github.openstream.ui.global.player.model.SheetBodyPage
import com.github.openstream.ui.global.player.model.VideoLocalState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SheetBodyPager(
    queue: List<VideoItem>,
    isPlaying: Boolean,
    sheetDragProgress: Float,
    fetchingState: OpenStreamMediaPlayer.FetchingState,
    currentVideoData: VideoData?,
    currentVideo: VideoItem?,
    currentQuality: VideoQuality?,
    currentPosition: Long,
    scope: CoroutineScope,
    videoLocalState: VideoLocalState,
    isAudioOnlyModeEnabled: Boolean,
    toChannelScreen: (String) -> Unit,
    toggleVideoWatchLater: () -> Unit,
    toggleVideoLiked: () -> Unit,
    switchPlaybackQuality: (VideoOption) -> Unit,
    subscribe: (ChannelItem.OnlineChannelItem) -> Unit,
    collapseMiniPlayer: () -> Unit,
) {
    val pagerState = rememberPagerState { SheetBodyPage.entries.size }
    if (sheetDragProgress != 0f) Column(
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
                SheetBodyPage.VideoDescription.ordinal ->
                    VideoDescriptionPage(
                        subscribe = subscribe,
                        collapseMiniPlayer = collapseMiniPlayer,
                        fetchingState = fetchingState,
                        currentVideoData = currentVideoData,
                        toChannelScreen = toChannelScreen,
                        toggleVideoWatchLater = toggleVideoWatchLater,
                        toggleVideoLiked = toggleVideoLiked,
                        videoLocalState = videoLocalState,
                        switchPlaybackQuality = switchPlaybackQuality,
                        currentQuality = currentQuality,
                    )

                SheetBodyPage.Queue.ordinal ->
                    QueuePage(
                        isPlaying = isPlaying,
                        queue = queue,
                        currentVideo = currentVideo,
                        currentPosition = currentPosition,
                        isAudioOnlyModeEnabled = isAudioOnlyModeEnabled,
                    )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            SheetBodyPage.entries.forEach { tab ->
                IconButton(
                    onClick = { scope.launch { pagerState.scrollToPage(page = tab.ordinal) } },
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


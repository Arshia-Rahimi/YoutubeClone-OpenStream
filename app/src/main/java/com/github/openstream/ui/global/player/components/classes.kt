package com.github.openstream.ui.global.player.components

import androidx.annotation.DrawableRes
import com.github.openstream.R

data class VideoPlaylistsState(
    val isInWatchLater: Boolean = false,
    val isLiked: Boolean = false,
)

enum class SheetBodyPage(
    @param:DrawableRes val icon: Int,
    @param:DrawableRes val selectedIcon: Int,
) {
    VideoDescription(
        icon = R.drawable.video_description,
        selectedIcon = R.drawable.video_description_selected,
    ),
    Queue(
        icon = R.drawable.queue,
        selectedIcon = R.drawable.queue_selected,
    ),
}

enum class PlayerSheetState {
    MINI_PLAYER, EXPANDED,
}

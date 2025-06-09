package com.github.openstream.ui.feature.subscriptions.root.components

import androidx.annotation.StringRes
import com.github.openstream.R

enum class SubscriptionsPage(
    @StringRes val title: Int,
) {
    VIDEOS(
        title = R.string.videos,
    ),
    CHANNELS(
        title = R.string.channels,
    ),
}
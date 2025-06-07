package com.github.arshiarahimi.openstream.ui.feature.subscriptions

import androidx.annotation.StringRes
import com.github.arshiarahimi.openstream.R

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

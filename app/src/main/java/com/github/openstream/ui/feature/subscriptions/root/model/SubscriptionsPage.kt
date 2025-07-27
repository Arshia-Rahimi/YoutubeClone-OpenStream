package com.github.openstream.ui.feature.subscriptions.root.model

import androidx.annotation.StringRes
import com.github.openstream.R

enum class SubscriptionsPage(
    @param:StringRes val title: Int,
) {
    VIDEOS(R.string.videos),
    CHANNELS(R.string.channels),
}
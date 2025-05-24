package com.github.arshiarahimi.openstream.app.navigation.routes

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.github.arshiarahimi.openstream.R
import com.github.arshiarahimi.openstream.core.model.dataitem.PlaylistItem
import kotlinx.serialization.Serializable

@Serializable
sealed class Tabs(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    @DrawableRes val selectedIcon: Int,
) {
    @Serializable
    data object Search : Tabs(
        title = R.string.search,
        icon = R.drawable.search,
        selectedIcon = R.drawable.search_selected,
    ) {
        @Serializable
        data object Root

        @Serializable
        data class Channel(val url: String)

        @Serializable
        data class Playlist(val playlist: PlaylistItem)
    }

    @Serializable
    data object Library : Tabs(
        title = R.string.library,
        icon = R.drawable.library,
        selectedIcon = R.drawable.library_selected,
    ) {
        @Serializable
        data object Root

        @Serializable
        data class Channel(val url: String)

        @Serializable
        data class Playlist(val playlist: PlaylistItem)
    }

    @Serializable
    data object Subscriptions : Tabs(
        title = R.string.subs,
        icon = R.drawable.subs,
        selectedIcon = R.drawable.subs_selected,
    ) {
        @Serializable
        data object Root

        @Serializable
        data class Channel(val url: String)

        @Serializable
        data class Playlist(val playlist: PlaylistItem)

        @Serializable
        data object SubscribedChannels
    }

    @Serializable
    data object Downloads : Tabs(
        title = R.string.downloads,
        icon = R.drawable.downloads,
        selectedIcon = R.drawable.downlaods_selected,
    )

    @Serializable
    data object Settings : Tabs(
        title = R.string.settings,
        icon = R.drawable.settings,
        selectedIcon = R.drawable.settings_selected,
    )

}

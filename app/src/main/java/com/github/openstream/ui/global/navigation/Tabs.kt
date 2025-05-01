package com.github.openstream.ui.global.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.arshia.openstream.R
import com.github.openstream.core.common.util.sendPulse
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.ui.feature.search.SearchViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.Serializable

@Serializable
sealed class Tabs(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    @DrawableRes val selectedIcon: Int,
    val isInTabRoot: MutableStateFlow<Boolean> = MutableStateFlow(true),
    val tabRootDoubleClickAction: (() -> Unit)? = null,
    val isInTabRootAction: (() -> Unit)? = null,
    var navigateToCurrentTabRoot: (() -> Unit)? = null,
) {
    @Serializable
    data object Search : Tabs(
        title = R.string.search,
        icon = R.drawable.search,
        selectedIcon = R.drawable.search_selected,
        tabRootDoubleClickAction = { SearchViewModel.searchFieldFocusEvent.sendPulse() },
        isInTabRootAction = { SearchViewModel.scrollToTopEvent.sendPulse() }
    ) {
        @Serializable
        data object Root

        @Serializable
        data class Channel(val url: String)

        @Serializable
        data class Playlist(val playlist: DataItem.Playlist)
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
        data class Playlist(val playlist: DataItem.Playlist)
    }

    @Serializable
    data object Subscriptions : Tabs(
        title = R.string.subs,
        icon = R.drawable.subs,
        selectedIcon = R.drawable.subs_selected,
    ) {
        @Serializable
        data object Root
    }

    @Serializable
    data object Downloads : Tabs(
        title = R.string.downloads,
        icon = R.drawable.downloads,
        selectedIcon = R.drawable.downlaods_selected,
    ) {
        @Serializable
        data object Root
    }

    @Serializable
    data object Settings : Tabs(
        title = R.string.settings,
        icon = R.drawable.settings,
        selectedIcon = R.drawable.settings_selected,
    ) {
        @Serializable
        data object Root
    }

}

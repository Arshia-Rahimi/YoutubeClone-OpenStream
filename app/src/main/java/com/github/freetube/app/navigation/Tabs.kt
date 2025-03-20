package com.github.freetube.app.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import cafe.adriel.voyager.navigator.tab.Tab
import com.arshia.freetube.R
import com.github.freetube.ui.feature.downloads.DownloadsTab
import com.github.freetube.ui.feature.library.LibraryTab
import com.github.freetube.ui.feature.search.SearchTab
import com.github.freetube.ui.feature.settings.SettingsTab
import com.github.freetube.ui.feature.subscriptions.SubscriptionsTab
import kotlinx.serialization.Serializable

@Serializable
enum class Tabs(
    val singleton: Tab,
    @DrawableRes val icon: Int,
    @DrawableRes val selectedIcon: Int,
    @StringRes val title: Int,
) {
    Search(
        icon = R.drawable.search,
        selectedIcon = R.drawable.search_selected,
        title = R.string.search,
        singleton = SearchTab,
    ),
    Library(
        icon = R.drawable.library,
        selectedIcon = R.drawable.library_selected,
        title = R.string.library,
        singleton = LibraryTab,
    ),
    Subscriptions(
        icon = R.drawable.subs,
        selectedIcon = R.drawable.subs_selected,
        title = R.string.subs,
        singleton = SubscriptionsTab,
    ),
    Downloads(
        icon = R.drawable.downloads,
        selectedIcon = R.drawable.downlaods_selected,
        title = R.string.downloads,
        singleton = DownloadsTab,
    ),
    Settings(
        icon = R.drawable.settings,
        selectedIcon = R.drawable.settings_selected,
        title = R.string.settings,
        singleton = SettingsTab,
    )
}

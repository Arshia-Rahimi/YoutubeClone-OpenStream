package com.github.freetube.app.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.arshia.freetube.R
import kotlinx.serialization.Serializable

enum class Tabs(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    @DrawableRes val selectedIcon: Int,
    val route: TopLevelDestination,
) {
    Search(
        title = R.string.search,
        icon = R.drawable.search,
        selectedIcon = R.drawable.search_selected,
        route = SearchRoute,
    ),
    Library(
        title = R.string.library,
        icon = R.drawable.library,
        selectedIcon = R.drawable.library_selected,
        route = LibraryRoute,
    ),
    Subscriptions(
        title = R.string.subs,
        icon = R.drawable.subs,
        selectedIcon = R.drawable.subs_selected,
        route = SubscriptionsRoute,
    ),
    Downloads(
        title = R.string.downloads,
        icon = R.drawable.downloads,
        selectedIcon = R.drawable.downlaods_selected,
        route = DownloadsRoute,
    ),
    Settings(
        title = R.string.settings,
        icon = R.drawable.settings,
        selectedIcon = R.drawable.settings_selected,
        route = SettingsRoute,
    ),
}

interface Route

interface TopLevelDestination : Route

fun Route.className() = this::class.toString().split(".").last()

@Serializable
data object SearchRoute : TopLevelDestination {
    @Serializable
    data object Main : Route

    @Serializable
    data class Channel(val url: String) : Route

    @Serializable
    data class Playlist(val url: String) : Route
}

@Serializable
data object LibraryRoute : TopLevelDestination {
    @Serializable
    data object Main : Route
}

@Serializable
data object SubscriptionsRoute : TopLevelDestination {
    @Serializable
    data object Main : Route
}

@Serializable
data object DownloadsRoute : TopLevelDestination {
    @Serializable
    data object Main : Route
}

@Serializable
data object SettingsRoute : TopLevelDestination {
    @Serializable
    data object Main : Route
}

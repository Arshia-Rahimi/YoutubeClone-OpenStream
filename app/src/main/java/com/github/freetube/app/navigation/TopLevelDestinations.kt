package com.github.freetube.app.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.arshia.freetube.R

enum class TopLevelDestinations(
    val route: Any,
    val mainRoute: Any,
    @DrawableRes val icon: Int,
    @DrawableRes val selectedIcon: Int,
    @StringRes val label: Int,
) {
    Search(
        route = LibreTubeRoutes.Search,
        mainRoute = LibreTubeRoutes.Search.Main,
        icon = R.drawable.search,
        selectedIcon = R.drawable.search_selected,
        label = R.string.search,
    ),
    Library(
        route = LibreTubeRoutes.Library,
        mainRoute = LibreTubeRoutes.Library.Main,
        icon = R.drawable.library,
        selectedIcon = R.drawable.library_selected,
        label = R.string.library,
    ),
    Subscriptions(
        route = LibreTubeRoutes.Subscriptions,
        mainRoute = LibreTubeRoutes.Subscriptions.Main,
        icon = R.drawable.subs,
        selectedIcon = R.drawable.subs_selected,
        label = R.string.subs,
    ),
    Downloads(
        route = LibreTubeRoutes.Downloads,
        mainRoute = LibreTubeRoutes.Downloads.Main,
        icon = R.drawable.downloads,
        selectedIcon = R.drawable.downlaods_selected,
        label = R.string.downloads,
    ),
    Settings(
        route = LibreTubeRoutes.Settings,
        mainRoute = LibreTubeRoutes.Settings.Main,
        icon = R.drawable.settings,
        selectedIcon = R.drawable.settings_selected,
        label = R.string.settings,
    )
}

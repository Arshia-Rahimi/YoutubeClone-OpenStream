package com.github.freetube.app.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.arshia.freetube.R

enum class TopLevelDestinations(
    val route: Any,
    @DrawableRes val icon: Int,
    @DrawableRes val selectedIcon: Int,
    @StringRes val label: Int,
) {
    Search(
        route = LibreTubeRoutes.Search,
        icon = R.drawable.search,
        selectedIcon = R.drawable.search_red,
        label = R.string.search,
    ),
    Downloads(
        route = LibreTubeRoutes.Downloads,
        icon = R.drawable.downloads,
        selectedIcon = R.drawable.downlaods_red,
        label = R.string.downloads,
    ),
    Subscriptions(
        route = LibreTubeRoutes.Subscriptions,
        icon = R.drawable.subs,
        selectedIcon = R.drawable.subs_red,
        label = R.string.subs,
    ),
    Library(
        route = LibreTubeRoutes.Library,
        icon = R.drawable.library,
        selectedIcon = R.drawable.library_red,
        label = R.string.library,
    ),
    Settings(
        route = LibreTubeRoutes.Settings,
        icon = R.drawable.settings,
        selectedIcon = R.drawable.settings_red,
        label = R.string.settings,
    )
}

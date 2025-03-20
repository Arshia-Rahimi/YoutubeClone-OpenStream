package com.github.freetube.app.rootcomponent

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.arshia.freetube.R
import com.github.freetube.app.navigation.LibreTubeRoutes
import kotlinx.serialization.Serializable

@Serializable
enum class Tabs(
    val route: Any,
    @DrawableRes val icon: Int,
    @DrawableRes val selectedIcon: Int,
    @StringRes val label: Int,
) {
    Search(
        route = LibreTubeRoutes.Search,
        icon = R.drawable.search,
        selectedIcon = R.drawable.search_selected,
        label = R.string.search,
    ),
    Library(
        route = LibreTubeRoutes.Library,
        icon = R.drawable.library,
        selectedIcon = R.drawable.library_selected,
        label = R.string.library,
    ),
    Subscriptions(
        route = LibreTubeRoutes.Subscriptions,
        icon = R.drawable.subs,
        selectedIcon = R.drawable.subs_selected,
        label = R.string.subs,
    ),
    Downloads(
        route = LibreTubeRoutes.Downloads,
        icon = R.drawable.downloads,
        selectedIcon = R.drawable.downlaods_selected,
        label = R.string.downloads,
    ),
    Settings(
        route = LibreTubeRoutes.Settings,
        icon = R.drawable.settings,
        selectedIcon = R.drawable.settings_selected,
        label = R.string.settings,
    )
}

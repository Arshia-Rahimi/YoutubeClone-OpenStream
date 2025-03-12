package com.github.freetube.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class TopLevelDestinations(
    val icon: ImageVector,
    val route: Any,
) {
    Search(
        icon = Icons.Default.Search,
        route = LibreTubeRoutes.Search,
    ),
    Subscriptions(
        icon = Icons.Default.Notifications,
        route = LibreTubeRoutes.Subscriptions,
    ),
    Playlists(
        Icons.AutoMirrored.Default.List,
        route = LibreTubeRoutes.Playlists,
    ),
    Settings(
        icon = Icons.Default.Settings,
        route = LibreTubeRoutes.Settings,
    )
}

package com.github.freetube.app.navigation

import kotlinx.serialization.Serializable

sealed interface LibreTubeRoutes {
    @Serializable
    data object Search

    @Serializable
    data object Subscriptions
    
    @Serializable
    data object Playlists
    
    @Serializable
    data object Settings
}

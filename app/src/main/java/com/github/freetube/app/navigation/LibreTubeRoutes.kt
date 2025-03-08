package com.github.freetube.app.navigation

import kotlinx.serialization.Serializable

sealed interface LibreTubeRoutes {
    @Serializable
    data object Home
    
    @Serializable
    data object Settings
}

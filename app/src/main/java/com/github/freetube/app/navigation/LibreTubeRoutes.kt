package com.github.freetube.app.navigation

import kotlinx.serialization.Serializable

sealed interface LibreTubeRoutes {
    @Serializable
    data object Search {
        @Serializable
        data object Main

        @Serializable
        data object Channel

        @Serializable
        data object Playlist
    }

    @Serializable
    data object Subscriptions {
        @Serializable
        data object Main

        @Serializable
        data object Channel

        @Serializable
        data object Playlist
    }
    
    @Serializable
    data object Library {
        @Serializable
        data object Main

        @Serializable
        data object Channel

        @Serializable
        data object Playlist
    }

    @Serializable
    data object Settings {
        @Serializable
        data object Main
    }

    @Serializable
    data object Downloads {
        @Serializable
        data object Main
    }
}

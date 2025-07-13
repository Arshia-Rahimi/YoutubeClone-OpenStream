package com.github.openstream.app.navigation.routes

import com.github.openstream.core.common.compose.Navigation
import com.github.openstream.core.model.dataitem.PlaylistItem

object OpenStreamNavTypes {
    val playlistType = Navigation<PlaylistItem>(PlaylistItem.serializer())
}

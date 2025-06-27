package com.github.openstream.app.navigation.routes

import com.github.openstream.core.common.compose.GenericNavType
import com.github.openstream.core.model.dataitem.PlaylistItem

object OpenStreamNavTypes {
    val playlistType = GenericNavType<PlaylistItem>(PlaylistItem.serializer())
}

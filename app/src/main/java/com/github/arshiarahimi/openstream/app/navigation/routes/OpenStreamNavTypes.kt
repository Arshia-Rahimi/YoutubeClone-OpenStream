package com.github.arshiarahimi.openstream.app.navigation.routes

import com.github.arshiarahimi.openstream.core.common.compose.GenericNavType
import com.github.arshiarahimi.openstream.core.model.dataitem.PlaylistItem

object OpenStreamNavTypes {
    val playlistType = GenericNavType<PlaylistItem>(PlaylistItem.serializer())
}

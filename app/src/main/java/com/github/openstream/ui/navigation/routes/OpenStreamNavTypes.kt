package com.github.openstream.ui.navigation.routes

import com.github.openstream.core.common.compose.GenericNavType
import com.github.openstream.core.shared.dataitem.PlaylistItem

object OpenStreamNavTypes {
    val playlistType = GenericNavType(PlaylistItem.serializer())
}

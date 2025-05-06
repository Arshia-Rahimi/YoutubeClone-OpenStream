package com.github.openstream.ui.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.github.openstream.core.model.extractordata.DataItem
import kotlinx.serialization.json.Json

object OpenStreamNavTypes {

    val playlistType = object : NavType<DataItem.Playlist>(false) {
        override fun get(
            bundle: Bundle,
            key: String
        ): DataItem.Playlist? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun put(
            bundle: Bundle,
            key: String,
            value: DataItem.Playlist
        ) {
            bundle.putString(key, Json.encodeToString(value))
        }

        override fun serializeAsValue(value: DataItem.Playlist): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun parseValue(value: String): DataItem.Playlist {
            return Json.decodeFromString(Uri.decode(value))
        }
    }
}

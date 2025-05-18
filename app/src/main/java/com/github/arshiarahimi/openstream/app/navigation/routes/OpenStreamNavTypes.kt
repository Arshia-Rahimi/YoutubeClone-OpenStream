package com.github.arshiarahimi.openstream.app.navigation.routes

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.github.arshiarahimi.openstream.core.model.extractordata.PlaylistItem
import kotlinx.serialization.json.Json

object OpenStreamNavTypes {

    val playlistType = object : NavType<PlaylistItem>(false) {
        override fun get(
            bundle: Bundle,
            key: String
        ): PlaylistItem? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun put(
            bundle: Bundle,
            key: String,
            value: PlaylistItem
        ) {
            bundle.putString(key, Json.encodeToString(value))
        }

        override fun serializeAsValue(value: PlaylistItem): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun parseValue(value: String): PlaylistItem {
            return Json.decodeFromString(Uri.decode(value))
        }
    }
}

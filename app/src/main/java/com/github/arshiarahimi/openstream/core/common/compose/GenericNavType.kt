package com.github.arshiarahimi.openstream.core.common.compose

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

class GenericNavType<T>(
    private val serializer: KSerializer<T>,
    nullable: Boolean = false,
) : NavType<T?>(nullable) {
    override fun get(
        bundle: Bundle,
        key: String
    ): T? {
        return Json.decodeFromString(serializer, bundle.getString(key) ?: return null)
    }

    override fun put(
        bundle: Bundle,
        key: String,
        value: T?,
    ) {
        if (value != null || !isNullableAllowed) bundle.putString(
            key,
            Json.encodeToString(serializer, value!!)
        )
    }

    override fun serializeAsValue(value: T?): String {
        return Uri.encode(Json.encodeToString(serializer, value!!))
    }

    override fun parseValue(value: String): T? {
        return Json.decodeFromString(serializer, Uri.decode(value))
    }
}

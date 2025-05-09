package com.github.openstream.core.common.util

import android.util.Log
import androidx.datastore.core.Serializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

class DataStoreSerializer<T : @Serializable Any>(
    override val defaultValue: T,
    private val serializer: KSerializer<T>,
) : Serializer<T> {

    override suspend fun readFrom(input: InputStream): T =
        try {
            Json.decodeFromString(
                deserializer = serializer,
                string = input.readBytes().decodeToString()
            )
        } catch (e: Exception) {
            defaultValue.also { Log.e("DataStoreSerializer", e.localizedMessage ?: "error") }
        }

    override suspend fun writeTo(t: T, output: OutputStream) =
        output.write(
            Json.encodeToString(
                serializer = serializer,
                value = t,
            ).encodeToByteArray()
        )
}

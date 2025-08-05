package com.github.openstream.core.common.datastore

import android.util.Log
import androidx.datastore.core.Serializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

class GenericDataStoreSerializer<T : @Serializable DataStoreModel>(
    override val defaultValue: T,
    private val serializer: KSerializer<T>,
) : Serializer<T> {
    
    override suspend fun readFrom(input: InputStream): T =
        try {
            Json.Default.decodeFromString(
                deserializer = serializer,
                string = input.readBytes().decodeToString()
            )
        } catch (e: Exception) {
            Log.e("DataStoreSerializer", e.localizedMessage ?: "error")
            defaultValue
        }
    
    override suspend fun writeTo(t: T, output: OutputStream) =
        output.write(
            Json.Default.encodeToString(
                serializer = serializer,
                value = t,
            ).encodeToByteArray()
        )
    
}

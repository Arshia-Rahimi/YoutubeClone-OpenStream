package com.github.openstream.core.datastore.proto.playerconfig

import android.util.Log
import androidx.datastore.core.Serializer
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

class PlayerConfigDataStoreModelSerializer : Serializer<PlayerConfigDataStoreModel> {
    override val defaultValue = PlayerConfigDataStoreModel()

    override suspend fun readFrom(input: InputStream): PlayerConfigDataStoreModel =
        try {
            Json.decodeFromString(
                deserializer = PlayerConfigDataStoreModel.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: Exception) {
            defaultValue.also { Log.e("readPlayerConfig", e.localizedMessage ?: "error") }
        }

    override suspend fun writeTo(t: PlayerConfigDataStoreModel, output: OutputStream) =
        output.write(
            Json.encodeToString(
                serializer = PlayerConfigDataStoreModel.serializer(),
                value = t
            ).encodeToByteArray()
        )
}

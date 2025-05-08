package com.github.openstream.core.datastore

import android.util.Log
import androidx.datastore.core.Serializer
import com.github.openstream.core.media3.PlayerRepeatMode
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Serializable
data class PlayerConfigModel(
    val seekIncrement: Long = 10000L,
    val playerRepeatMode: PlayerRepeatMode = PlayerRepeatMode.OFF,
    val isPlaylistShuffleEnabled: Boolean = false,
    val playbackSpeed: Float = 1F,
)

class PlayerConfigSerializer : Serializer<PlayerConfigModel> {
    override val defaultValue = PlayerConfigModel()

    override suspend fun readFrom(input: InputStream): PlayerConfigModel =
        try {
            Json.decodeFromString(
                deserializer = PlayerConfigModel.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: Exception) {
            defaultValue.also { Log.e("readPlayerConfig", e.localizedMessage ?: "error") }
        }

    override suspend fun writeTo(t: PlayerConfigModel, output: OutputStream) =
        output.write(
            Json.encodeToString(
                serializer = PlayerConfigModel.serializer(),
                value = t
            ).encodeToByteArray()
        )
}

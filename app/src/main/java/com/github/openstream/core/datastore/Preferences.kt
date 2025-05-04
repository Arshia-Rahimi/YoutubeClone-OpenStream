package com.github.openstream.core.datastore

import android.util.Log
import androidx.datastore.core.Serializer
import com.github.openstream.core.media3.PlayerRepeatMode
import com.github.openstream.ui.feature.library.components.SortType
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Serializable
data class PreferencesModel(
    val seekIncrement: Long = 10000L,
    val playerRepeatMode: PlayerRepeatMode = PlayerRepeatMode.OFF,
    val isPlaylistShuffleEnabled: Boolean = false,
    val playbackSpeed: Float = 1F,
    val librarySortType: SortType = SortType.CREATED_AT_ASC,
)

class PreferencesSerializer: Serializer<PreferencesModel> {
    override val defaultValue = PreferencesModel()
    
    override suspend fun readFrom(input: InputStream): PreferencesModel =
        try {
            Json.decodeFromString(
                deserializer = PreferencesModel.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: Exception) {
            defaultValue.also { Log.e("readPlayerConfig", e.localizedMessage ?: "error") }
        }
    
    override suspend fun writeTo(t: PreferencesModel, output: OutputStream) =
        output.write(
            Json.encodeToString(
                serializer = PreferencesModel.serializer(),
                value = t
            ).encodeToByteArray()
        )
}

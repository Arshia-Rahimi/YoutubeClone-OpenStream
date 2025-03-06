package com.github.freetube.core.datastore

import android.util.Log
import androidx.datastore.core.Serializer
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream


class LibreTubeDataStoreModelSerializer: Serializer<LibreTubeDataStoreModel> {

    override val defaultValue = LibreTubeDataStoreModel()

    override suspend fun readFrom(input: InputStream): LibreTubeDataStoreModel {
        return try {
            Json.decodeFromString(
                deserializer = LibreTubeDataStoreModel.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: Exception) {
            defaultValue.also {
                Log.e("readUserData", e.localizedMessage ?: "error")
            }
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun writeTo(t: LibreTubeDataStoreModel, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = LibreTubeDataStoreModel.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}

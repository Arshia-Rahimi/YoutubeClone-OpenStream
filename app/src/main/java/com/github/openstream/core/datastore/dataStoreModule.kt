package com.github.openstream.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import com.github.openstream.core.common.util.DataStoreSerializer
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataStoreModule = module {
    single<DataStore<PreferencesModel>> {
        DataStoreFactory.create(
            serializer = DataStoreSerializer(
                defaultValue = PreferencesModel(),
                serializer = PreferencesModel.serializer(),
            ),
            produceFile = { androidContext().dataStoreFile("preferences.pb") },
            corruptionHandler = ReplaceFileCorruptionHandler { PreferencesModel() },
        )
    }

    single<DataStore<PlayerConfigModel>> {
        DataStoreFactory.create(
            serializer = DataStoreSerializer(
                defaultValue = PlayerConfigModel(),
                serializer = PlayerConfigModel.serializer(),
            ),
            produceFile = { androidContext().dataStoreFile("player_config.pb") },
            corruptionHandler = ReplaceFileCorruptionHandler { PlayerConfigModel() },
        )
    }
}

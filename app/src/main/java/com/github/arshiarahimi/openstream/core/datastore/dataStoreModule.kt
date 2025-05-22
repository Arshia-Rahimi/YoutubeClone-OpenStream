package com.github.arshiarahimi.openstream.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import com.github.arshiarahimi.openstream.core.common.util.GenericDataStoreSerializer
import com.github.arshiarahimi.openstream.core.shared.PLAYER_CONFIG_QUALIFIER
import com.github.arshiarahimi.openstream.core.shared.PREFERENCES_QUALIFIER
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataStoreModule = module {
    single<DataStore<PreferencesModel>>(named(PREFERENCES_QUALIFIER)) {
        DataStoreFactory.create(
            serializer = GenericDataStoreSerializer(
                defaultValue = PreferencesModel(),
                serializer = PreferencesModel.serializer(),
            ),
            produceFile = { androidContext().dataStoreFile("$PREFERENCES_QUALIFIER.pb") },
            corruptionHandler = ReplaceFileCorruptionHandler { PreferencesModel() },
        )
    }

    single<DataStore<PlayerConfigModel>>(named(PLAYER_CONFIG_QUALIFIER)) {
        DataStoreFactory.create(
            serializer = GenericDataStoreSerializer(
                defaultValue = PlayerConfigModel(),
                serializer = PlayerConfigModel.serializer(),
            ),
            produceFile = { androidContext().dataStoreFile("$PLAYER_CONFIG_QUALIFIER.pb") },
            corruptionHandler = ReplaceFileCorruptionHandler { PlayerConfigModel() },
        )
    }
}

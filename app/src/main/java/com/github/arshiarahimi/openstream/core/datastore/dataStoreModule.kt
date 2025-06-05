package com.github.arshiarahimi.openstream.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import com.github.arshiarahimi.openstream.core.common.util.GenericDataStoreSerializer
import com.github.arshiarahimi.openstream.core.shared.KoinQualifiers
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataStoreModule = module {
    single<DataStore<PreferencesModel>>(named(KoinQualifiers.PREFERENCES)) {
        DataStoreFactory.create(
            serializer = GenericDataStoreSerializer(
                defaultValue = PreferencesModel(),
                serializer = PreferencesModel.serializer(),
            ),
            produceFile = { androidContext().dataStoreFile("${KoinQualifiers.PREFERENCES}.pb") },
            corruptionHandler = ReplaceFileCorruptionHandler { PreferencesModel() },
        )
    }

    single<DataStore<PlayerConfigModel>>(named(KoinQualifiers.PLAYER_CONFIG)) {
        DataStoreFactory.create(
            serializer = GenericDataStoreSerializer(
                defaultValue = PlayerConfigModel(),
                serializer = PlayerConfigModel.serializer(),
            ),
            produceFile = { androidContext().dataStoreFile("${KoinQualifiers.PLAYER_CONFIG}.pb") },
            corruptionHandler = ReplaceFileCorruptionHandler { PlayerConfigModel() },
        )
    }

    single<DataStore<QueueModel>>(named(KoinQualifiers.QUEUE)) {
        DataStoreFactory.create(
            serializer = GenericDataStoreSerializer(
                defaultValue = QueueModel(),
                serializer = QueueModel.serializer(),
            ),
            produceFile = { androidContext().dataStoreFile("${KoinQualifiers.QUEUE}.pb") },
            corruptionHandler = ReplaceFileCorruptionHandler { QueueModel() },
        )
    }
}

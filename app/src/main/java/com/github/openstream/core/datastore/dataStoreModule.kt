package com.github.openstream.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import com.github.openstream.core.common.datastore.GenericDataStoreSerializer
import com.github.openstream.core.shared.KoinQualifiers
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
}

package com.github.openstream.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataStoreModule = module {
    single<DataStore<PreferencesModel>> {
        DataStoreFactory.create(
            serializer = PreferencesSerializer(),
            produceFile = { androidContext().dataStoreFile("open_stream_preferences.pb") },
            corruptionHandler = ReplaceFileCorruptionHandler { PreferencesModel() },
        )
    }
}

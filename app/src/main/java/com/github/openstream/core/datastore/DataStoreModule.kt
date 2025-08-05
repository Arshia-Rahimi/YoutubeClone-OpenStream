package com.github.openstream.core.datastore

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import com.github.openstream.core.common.datastore.GenericDataStoreSerializer
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataStoreModule = module {
    single(named(PreferencesModel.NAME)) {
        DataStoreFactory.create(
            serializer = GenericDataStoreSerializer(PreferencesModel(), PreferencesModel.serializer()),
            produceFile = { androidContext().dataStoreFile("${PreferencesModel.NAME}.pb") },
            corruptionHandler = ReplaceFileCorruptionHandler { PreferencesModel() },
        )
    }
}

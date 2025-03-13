package com.github.freetube.core.datastore.di

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import com.github.freetube.core.datastore.LibreTubeDataStore
import com.github.freetube.core.datastore.LibreTubeDataStoreModel
import com.github.freetube.core.datastore.LibreTubeDataStoreModelSerializer
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataStoreModule = module {
    single<DataStore<LibreTubeDataStoreModel>> {
        DataStoreFactory.create(
            serializer = LibreTubeDataStoreModelSerializer(),
            produceFile = { androidContext().dataStoreFile("freeTube.pb") },
            corruptionHandler = ReplaceFileCorruptionHandler { _ -> LibreTubeDataStoreModel() },
        )
    }
    
    singleOf(::LibreTubeDataStore)
}

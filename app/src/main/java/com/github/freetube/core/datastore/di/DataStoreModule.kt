package com.github.freetube.core.datastore.di

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import com.github.freetube.core.datastore.proto.playerconfig.PlayerConfigDataStore
import com.github.freetube.core.datastore.proto.playerconfig.PlayerConfigDataStoreModel
import com.github.freetube.core.datastore.proto.playerconfig.PlayerConfigDataStoreModelSerializer
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataStoreModule = module {
    single<DataStore<PlayerConfigDataStoreModel>> {
        DataStoreFactory.create(
            serializer = PlayerConfigDataStoreModelSerializer(),
            produceFile = { androidContext().dataStoreFile("libreTube_playerConfig.pb") },
            corruptionHandler = ReplaceFileCorruptionHandler { PlayerConfigDataStoreModel() },
        )
    }

    singleOf(::PlayerConfigDataStore)
}

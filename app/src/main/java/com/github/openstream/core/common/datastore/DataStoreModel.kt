package com.github.openstream.core.common.datastore

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.qualifier.named

@Serializable
abstract class DataStoreModel {
    val name = this::class.simpleName ?: "DataStoreModel"
}

inline fun <reified T : DataStoreModel> T.dataStoreSerializer() =
    GenericDataStoreSerializer(this, serializer())

fun DataStoreModel.dataStoreInstance(context: Context) = DataStoreFactory.create(
    serializer = dataStoreSerializer(),
    produceFile = { context.dataStoreFile("$name}.pb") },
    corruptionHandler = ReplaceFileCorruptionHandler { this },
)

context(module: Module)
fun DataStoreModel.dataStore() = module.single(named(name)) { dataStoreInstance(androidContext()) }

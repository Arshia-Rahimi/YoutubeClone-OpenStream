package com.github.openstream.core.datastore

import com.github.openstream.core.common.datastore.dataStore
import org.koin.dsl.module

val dataStoreModule = module {
    PreferencesModel().dataStore()
}

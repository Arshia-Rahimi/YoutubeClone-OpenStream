package com.github.openstream.app

import android.app.Application
import com.github.openstream.core.data.di.dataModule
import com.github.openstream.core.database.di.databaseModule
import com.github.openstream.core.datastore.dataStoreModule
import com.github.openstream.core.media3.di.media3Module
import com.github.openstream.ui.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.androix.startup.KoinStartup
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.KoinConfiguration

@OptIn(KoinExperimentalAPI::class)
class MainApplication: Application(), KoinStartup {
    
    override fun onKoinStartup() = KoinConfiguration {
        androidContext(this@MainApplication)
        modules(
            viewModelModule,
            dataModule,
            databaseModule,
            media3Module,
            dataStoreModule,
        )
    }

}

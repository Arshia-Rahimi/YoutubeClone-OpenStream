package com.github.freetube.app

import android.app.Application
import com.github.freetube.core.data.di.dataModule
import com.github.freetube.core.database.di.databaseModule
import com.github.freetube.core.datastore.di.dataStoreModule
import com.github.freetube.ui.di.screenModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.androix.startup.KoinStartup
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.KoinConfiguration

@OptIn(KoinExperimentalAPI::class)
class MainApplication: Application(), KoinStartup {
    
    override fun onKoinStartup() = KoinConfiguration {
        androidContext(this@MainApplication)
        modules(
            screenModelModule,
            dataModule,
            dataStoreModule,
            databaseModule,
        )
    }

}

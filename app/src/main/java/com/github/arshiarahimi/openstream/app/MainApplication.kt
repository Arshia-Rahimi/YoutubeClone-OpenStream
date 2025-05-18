package com.github.arshiarahimi.openstream.app

import android.app.Application
import com.github.arshiarahimi.openstream.core.data.di.dataModule
import com.github.arshiarahimi.openstream.core.database.di.databaseModule
import com.github.arshiarahimi.openstream.core.datastore.dataStoreModule
import com.github.arshiarahimi.openstream.core.media3.di.media3Module
import com.github.arshiarahimi.openstream.ui.di.viewModelModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.androix.startup.KoinStartup
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.KoinConfiguration
import org.koin.dsl.module

@OptIn(KoinExperimentalAPI::class)
class MainApplication : Application(), KoinStartup {

    override fun onKoinStartup() = KoinConfiguration {
        androidContext(this@MainApplication)
        modules(
            viewModelModule,
            dataModule,
            databaseModule,
            media3Module,
            dataStoreModule,
            appModule,
        )
    }

    private val appModule =
        module {
            factory {
                CoroutineScope(Dispatchers.IO + SupervisorJob())
            }
        }
}

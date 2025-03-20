package com.github.freetube.ui.feature.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions

object SettingsTab : Tab {

    private fun readResolve(): Any = SettingsTab

    override val options: TabOptions
        @Composable
        get() = remember {
            TabOptions(
                title = "Settings",
                index = 4u,
            )
        }

    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<SettingsScreenModel>()
        SettingsScreen(screenModel)
    }
}

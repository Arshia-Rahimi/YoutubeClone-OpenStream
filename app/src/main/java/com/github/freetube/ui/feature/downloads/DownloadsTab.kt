package com.github.freetube.ui.feature.downloads

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions

object DownloadsTab : Tab {

    private fun readResolve(): Any = DownloadsTab

    override val options: TabOptions
        @Composable
        get() = remember {
            TabOptions(
                index = 3u,
                title = "Downloads",
            )
        }

    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<DownloadsScreenModel>()
        DownloadsScreen(screenModel)
    }
}

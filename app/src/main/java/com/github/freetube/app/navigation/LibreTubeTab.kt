package com.github.freetube.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.github.freetube.ui.feature.downloads.DownloadsTab

abstract class LibreTubeTab : Tab {

    abstract val data: TabData

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(data.title)
            return remember {
                TabOptions(
                    index = DownloadsTab.data.index,
                    title = title,
                )
            }
        }
}

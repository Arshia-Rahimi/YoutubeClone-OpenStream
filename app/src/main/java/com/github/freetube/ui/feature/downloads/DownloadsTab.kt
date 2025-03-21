package com.github.freetube.ui.feature.downloads

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.arshia.freetube.R
import com.github.freetube.app.navigation.LibreTubeTab
import com.github.freetube.app.navigation.TabData

object DownloadsTab : LibreTubeTab {

    override val data = TabData(
        index = 3u,
        title = R.string.downloads,
        icon = R.drawable.downloads,
        selectedIcon = R.drawable.downlaods_selected,
    )
    private fun readResolve(): Any = DownloadsTab

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(data.title)
            return remember {
                TabOptions(
                    index = data.index,
                    title = title,
                )
            }
        }

    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<DownloadsScreenModel>()
        DownloadsScreen(screenModel)
    }

}

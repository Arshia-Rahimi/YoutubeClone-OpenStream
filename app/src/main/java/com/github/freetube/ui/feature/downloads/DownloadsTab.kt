package com.github.freetube.ui.feature.downloads

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.koin.koinScreenModel
import com.arshia.freetube.R
import com.github.freetube.app.navigation.LibreTubeTab
import com.github.freetube.app.navigation.TabData

object DownloadsTab : LibreTubeTab() {

    private fun readResolve(): Any = DownloadsTab

    override val data = TabData(
        index = 3u,
        title = R.string.downloads,
        icon = R.drawable.downloads,
        selectedIcon = R.drawable.downlaods_selected,
    )


    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<DownloadsScreenModel>()
        DownloadsScreenContent(screenModel)
    }

}

package com.github.freetube.ui.feature.library

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.koin.koinScreenModel
import com.arshia.freetube.R
import com.github.freetube.app.navigation.LibreTubeTab
import com.github.freetube.app.navigation.TabData

object LibraryTab : LibreTubeTab() {

    override val data = TabData(
        index = 1u,
        title = R.string.library,
        icon = R.drawable.library,
        selectedIcon = R.drawable.library_selected,
    )
    private fun readResolve(): Any = LibraryTab

    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<LibraryScreenModel>()
        LibraryScreen(screenModel)
    }
}

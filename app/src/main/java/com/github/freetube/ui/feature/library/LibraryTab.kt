package com.github.freetube.ui.feature.library

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions

object LibraryTab : Tab {

    private fun readResolve(): Any = LibraryTab

    override val options: TabOptions
        @Composable
        get() = remember {
            TabOptions(
                index = 1u,
                title = "Library",
            )
        }

    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<LibraryScreenModel>()
        LibraryScreen(screenModel)
    }
}

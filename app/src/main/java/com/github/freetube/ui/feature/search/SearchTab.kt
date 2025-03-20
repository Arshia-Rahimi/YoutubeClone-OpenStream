package com.github.freetube.ui.feature.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions

object SearchTab : Tab {

    private fun readResolve(): Any = SearchTab

    override val options: TabOptions
        @Composable
        get() = remember {
            TabOptions(
                title = "Search",
                index = 0u,
            )
        }

    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<SearchScreenModel>()
        SearchScreen(screenModel)
    }
}

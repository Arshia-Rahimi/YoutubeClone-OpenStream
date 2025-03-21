package com.github.freetube.ui.feature.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.arshia.freetube.R
import com.github.freetube.app.navigation.LibreTubeTab
import com.github.freetube.app.navigation.TabData

object SearchTab : LibreTubeTab {

    override val data = TabData(
        index = 0u,
        title = R.string.search,
        icon = R.drawable.search,
        selectedIcon = R.drawable.search_selected,
    )

    private fun readResolve(): Any = SearchTab

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(data.title)
            return remember {
                TabOptions(
                    title = title,
                    index = data.index,
                )
            }
        }

    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<SearchScreenModel>()
        SearchScreen(screenModel)
    }
}

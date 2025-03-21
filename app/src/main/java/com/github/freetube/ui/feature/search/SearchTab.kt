package com.github.freetube.ui.feature.search

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.NavigatorDisposeBehavior
import cafe.adriel.voyager.transitions.SlideTransition
import com.arshia.freetube.R
import com.github.freetube.app.navigation.LibreTubeTab
import com.github.freetube.app.navigation.TabData
import com.github.freetube.ui.feature.search.main.components.SearchTabScreen

object SearchTab : LibreTubeTab() {

    override val data = TabData(
        index = 0u,
        title = R.string.search,
        icon = R.drawable.search,
        selectedIcon = R.drawable.search_selected,
    )

    private fun readResolve(): Any = SearchTab

    @OptIn(ExperimentalVoyagerApi::class)
    @Composable
    override fun Content() {
        Navigator(
            screen = SearchTabScreen(),
            disposeBehavior = NavigatorDisposeBehavior(disposeSteps = false)
        ) { navigator ->
            SlideTransition(
                navigator = navigator,
                disposeScreenAfterTransitionEnd = true,
            )
        }
    }
}

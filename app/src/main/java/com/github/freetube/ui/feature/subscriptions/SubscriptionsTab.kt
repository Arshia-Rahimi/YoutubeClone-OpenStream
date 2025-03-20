package com.github.freetube.ui.feature.subscriptions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions

object SubscriptionsTab : Tab {

    private fun readResolve(): Any = SubscriptionsTab

    override val options: TabOptions
        @Composable
        get() = remember {
            TabOptions(
                index = 2u,
                title = "Subscriptions",
            )
        }

    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<SubscriptionsScreenModel>()
        SubscriptionsScreen(screenModel)
    }

}

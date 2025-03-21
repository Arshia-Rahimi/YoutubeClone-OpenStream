package com.github.freetube.ui.feature.subscriptions

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.koin.koinScreenModel
import com.arshia.freetube.R
import com.github.freetube.app.navigation.LibreTubeTab
import com.github.freetube.app.navigation.TabData

object SubscriptionsTab : LibreTubeTab() {

    override val data = TabData(
        index = 2u,
        title = R.string.subs,
        icon = R.drawable.subs,
        selectedIcon = R.drawable.subs_selected,
    )
    private fun readResolve(): Any = SubscriptionsTab

    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<SubscriptionsScreenModel>()
        SubscriptionsScreen(screenModel)
    }

}

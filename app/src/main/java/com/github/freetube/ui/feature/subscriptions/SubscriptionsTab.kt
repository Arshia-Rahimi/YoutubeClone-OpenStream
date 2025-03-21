package com.github.freetube.ui.feature.subscriptions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.arshia.freetube.R
import com.github.freetube.app.navigation.LibreTubeTab
import com.github.freetube.app.navigation.TabData

object SubscriptionsTab : LibreTubeTab {

    override val data = TabData(
        index = 2u,
        title = R.string.subs,
        icon = R.drawable.subs,
        selectedIcon = R.drawable.subs_selected,
    )
    private fun readResolve(): Any = SubscriptionsTab

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
        val screenModel = koinScreenModel<SubscriptionsScreenModel>()
        SubscriptionsScreen(screenModel)
    }

}

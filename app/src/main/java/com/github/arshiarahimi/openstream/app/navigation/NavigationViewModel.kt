package com.github.arshiarahimi.openstream.app.navigation

import androidx.lifecycle.ViewModel
import com.github.arshiarahimi.openstream.app.navigation.routes.Tabs
import com.github.arshiarahimi.openstream.app.navigation.routes.Tabs.Downloads
import com.github.arshiarahimi.openstream.app.navigation.routes.Tabs.Library
import com.github.arshiarahimi.openstream.app.navigation.routes.Tabs.Search
import com.github.arshiarahimi.openstream.app.navigation.routes.Tabs.Settings
import com.github.arshiarahimi.openstream.app.navigation.routes.Tabs.Subscriptions
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class NavigationViewModel : ViewModel() {

    companion object {
        val tabsList = arrayOf(
            Search,
            Library,
            Subscriptions,
            Downloads,
            Settings,
        )
    }

    val currentTab = MutableStateFlow<Tabs>(Subscriptions)

    private val _tabDoubleClickAction = Channel<Tabs>(1)
    val tabDoubleClickAction = _tabDoubleClickAction.receiveAsFlow()

    fun tabDoubleClick(tab: Tabs) {
        _tabDoubleClickAction.trySend(tab)
    }

    private val _tabClickAction = Channel<Tabs>(1)
    val tabClickAction = _tabClickAction.receiveAsFlow()

    fun tabClick(tab: Tabs) {
        _tabClickAction.trySend(tab)
    }
}

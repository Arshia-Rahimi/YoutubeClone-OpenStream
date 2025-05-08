package com.github.openstream.app.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.github.openstream.app.navigation.routes.Tabs
import com.github.openstream.app.navigation.routes.Tabs.Downloads
import com.github.openstream.app.navigation.routes.Tabs.Library
import com.github.openstream.app.navigation.routes.Tabs.Search
import com.github.openstream.app.navigation.routes.Tabs.Settings
import com.github.openstream.app.navigation.routes.Tabs.Subscriptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NavigationViewModel: ViewModel() {
    
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
    
    private val _topBar: MutableStateFlow<(@Composable () -> Unit)?> = MutableStateFlow(null)
    val topBar = _topBar.asStateFlow()
    
    fun setTopBar(newTopBar: @Composable () -> Unit) {
        _topBar.value = newTopBar
    }
}

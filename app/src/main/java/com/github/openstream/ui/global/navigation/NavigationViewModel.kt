package com.github.openstream.ui.global.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.github.openstream.ui.global.navigation.Tabs.Downloads
import com.github.openstream.ui.global.navigation.Tabs.Library
import com.github.openstream.ui.global.navigation.Tabs.Search
import com.github.openstream.ui.global.navigation.Tabs.Settings
import com.github.openstream.ui.global.navigation.Tabs.Subscriptions
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
    
    private val _currentTab = MutableStateFlow<Tabs>(Subscriptions)
    val currentTab = _currentTab.asStateFlow()
    
    private val _topBar: MutableStateFlow<(@Composable () -> Unit)?> = MutableStateFlow(null)
    val topBar = _topBar.asStateFlow()
    
    fun setCurrentTab(newTab: Tabs) {
        _currentTab.value = newTab
    }
    
    fun setTopBar(newTopBar: @Composable () -> Unit) {
        _topBar.value = newTopBar
    }
}

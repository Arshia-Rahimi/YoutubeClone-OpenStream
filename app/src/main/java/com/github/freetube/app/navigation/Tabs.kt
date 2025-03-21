package com.github.freetube.app.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import cafe.adriel.voyager.navigator.tab.Tab

interface LibreTubeTab : Tab {
    val data: TabData
}

data class TabData(
    val index: UShort,
    @DrawableRes val icon: Int,
    @DrawableRes val selectedIcon: Int,
    @StringRes val title: Int,
)

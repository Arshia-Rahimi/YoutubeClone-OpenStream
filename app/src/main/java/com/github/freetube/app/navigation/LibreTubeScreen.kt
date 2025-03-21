package com.github.freetube.app.navigation

import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey

abstract class LibreTubeScreen : Screen {
    override val key = uniqueScreenKey
}

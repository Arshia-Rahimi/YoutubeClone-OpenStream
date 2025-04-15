package com.github.freetube.core.common.compose

import androidx.navigation.NavController

fun NavController.getCurrentRouteClassName() =
    currentBackStackEntry?.destination?.route?.split(".")?.last()

fun NavController.popToRoot() {
    val root = graph.startDestinationRoute ?: return
    popBackStack(
        route = root,
        inclusive = false,
    )
}

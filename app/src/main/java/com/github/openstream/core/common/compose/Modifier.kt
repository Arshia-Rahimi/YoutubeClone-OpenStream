package com.github.openstream.core.common.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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

@Composable
fun Modifier.onCondition(
    condition: Boolean,
    onFalse: @Composable Modifier.() -> Modifier = { this },
    onTrue: @Composable Modifier.() -> Modifier
): Modifier =
    if (condition) onTrue() else onFalse()

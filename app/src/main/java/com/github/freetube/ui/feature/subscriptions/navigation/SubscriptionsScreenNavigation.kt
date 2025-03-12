package com.github.freetube.ui.feature.subscriptions.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.github.freetube.app.navigation.LibreTubeRoutes
import com.github.freetube.ui.feature.subscriptions.SubscriptionsScreen

fun NavController.navigateToSubscriptions(navOptions: NavOptions) =
    navigate(route = LibreTubeRoutes.Subscriptions, navOptions =  navOptions)

fun NavGraphBuilder.subscriptionsScreenNavigation() {
    composable<LibreTubeRoutes.Subscriptions> {
        SubscriptionsScreen()
    }
}

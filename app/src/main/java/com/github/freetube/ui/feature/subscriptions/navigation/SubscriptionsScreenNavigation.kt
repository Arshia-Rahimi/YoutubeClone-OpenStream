package com.github.freetube.ui.feature.subscriptions.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.freetube.app.navigation.LibreTubeRoutes
import com.github.freetube.ui.feature.subscriptions.SubscriptionsScreen

fun NavGraphBuilder.subscriptionsScreenNavigation() {
    composable<LibreTubeRoutes.Subscriptions> {
        SubscriptionsScreen()
    }
}

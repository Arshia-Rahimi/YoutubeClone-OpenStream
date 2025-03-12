package com.github.freetube.ui.feature.subscriptions

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionsScreen(
) {
    val viewModel = koinViewModel<SubscriptionsScreenViewModel>()

}

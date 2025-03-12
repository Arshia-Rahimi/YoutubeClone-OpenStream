package com.github.freetube.ui.feature.subscriptions.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import com.github.freetube.ui.designsystem.LibreTubeTopBar

@Composable
fun HomeScreenTopBar(
    title: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    LibreTubeTopBar(
        title = title,
        actions = actions,
    )
}

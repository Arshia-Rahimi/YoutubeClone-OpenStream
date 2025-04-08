package com.github.freetube.core.common.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Modifier.onCondition(
    condition: Boolean,
    action: @Composable Modifier.() -> Modifier
): Modifier =
    if (condition) action() else this

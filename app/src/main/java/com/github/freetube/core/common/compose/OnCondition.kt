package com.github.freetube.core.common.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Modifier.onCondition(
    condition: Boolean,
    onFalse: @Composable Modifier.() -> Modifier = { this },
    onTrue: @Composable Modifier.() -> Modifier
): Modifier =
    if (condition) onTrue() else onFalse()

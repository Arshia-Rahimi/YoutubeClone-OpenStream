package com.github.freetube.core.common.util

import androidx.compose.ui.Modifier

fun Modifier.onCondition(
    condition: Boolean,
    action: Modifier.() -> Modifier
): Modifier =
    if (condition) action() else this

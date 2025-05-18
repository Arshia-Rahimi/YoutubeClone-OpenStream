package com.github.arshiarahimi.openstream.core.common.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

fun <T> Flow<T>.onFirst(action: suspend (T) -> Unit): Flow<T> {
    var isFirst = true
    return onEach {
        if (isFirst) {
            isFirst = false
            action(it)
        }
    }
}

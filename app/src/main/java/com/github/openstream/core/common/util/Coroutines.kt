package com.github.openstream.core.common.util

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onEach

fun Channel<Unit>.sendPulse() = trySend(Unit)

fun MutableSharedFlow<Unit>.sendPulse() = tryEmit(Unit)

fun <T> Flow<T>.onFirst(action: suspend (T) -> Unit): Flow<T> {
    var isFirst = true
    return onEach {
        if (isFirst) {
            isFirst = false
            action(it)
        }
    }
}

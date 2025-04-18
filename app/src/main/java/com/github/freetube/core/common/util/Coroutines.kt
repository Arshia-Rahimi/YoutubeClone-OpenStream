package com.github.freetube.core.common.util

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow

fun Channel<Unit>.sendPulse() = trySend(Unit)

fun MutableSharedFlow<Unit>.sendPulse() = tryEmit(Unit)

package com.github.openstream.core.common.compose

import androidx.compose.material3.SnackbarDuration
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

data class SnackBarAction(
    val name: String,
    val action: () -> Unit,
)

data class SnackBarEvent(
    val message: String,
    val action: SnackBarAction? = null,
    val isImmediate: Boolean = false,
    val duration: SnackbarDuration = SnackbarDuration.Long,
)

object SnackBarController {
    private val _events = Channel<SnackBarEvent>()
    val events = _events.receiveAsFlow()

    suspend fun sendEvent(event: SnackBarEvent) {
        _events.send(event)
    }

    suspend fun sendEvent(message: String) {
        _events.send(SnackBarEvent(message))
    }
}

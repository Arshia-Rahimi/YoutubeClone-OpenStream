package com.github.openstream.ui.global.player

import com.github.openstream.core.media3.PlayerRepeatMode
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow


sealed interface PlayerAction {
    data object Start : PlayerAction
    data object TogglePlay : PlayerAction
    data object Next : PlayerAction
    data object Previous : PlayerAction
    data class SeekTo(val ms: Long) : PlayerAction
    data object SeekForward : PlayerAction
    data object SeekBackward : PlayerAction
    data class SetRepeatMode(val repeatMode: PlayerRepeatMode) : PlayerAction
    data object ToggleShuffleMode : PlayerAction
    data class SetPlaybackSpeed(val speed: Float) : PlayerAction
    
    fun send() {
        PlayerController.sendAction(this)
    }
}

object PlayerController {
    private val _events = Channel<PlayerAction>()
    val events = _events.receiveAsFlow()
    
    fun sendAction(action: PlayerAction) {
        _events.trySend(action)
    }
}

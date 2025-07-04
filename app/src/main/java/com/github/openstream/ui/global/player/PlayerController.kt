package com.github.openstream.ui.global.player

import com.github.openstream.core.model.dataitem.VideoItem
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow


sealed interface PlayerAction {
    data class Start(val videos: List<VideoItem>, val index: Int = 0) : PlayerAction
    data class SeekTo(val ms: Long) : PlayerAction
    data class SetPlaybackSpeed(val speed: Float) : PlayerAction
    data object ChangeRepeatMode : PlayerAction
    data object TogglePlay : PlayerAction
    data object Next : PlayerAction
    data object Previous : PlayerAction
    data object SeekForward : PlayerAction
    data object SeekBackward : PlayerAction
    
    fun send() = PlayerController.sendAction(this)
}

object PlayerController {
    private val _events = Channel<PlayerAction>()
    val events = _events.receiveAsFlow()
    
    fun sendAction(action: PlayerAction) {
        _events.trySend(action)
    }
}

package com.github.openstream.ui.global.components

import com.github.openstream.core.model.extractordata.DataItem
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object PopupController {
    private val _addToPlaylistEvent: MutableStateFlow<DataItem.Video?> = MutableStateFlow(null)
    val addToPlaylistEvent = _addToPlaylistEvent.asStateFlow()

    val createPlaylistDialogEvent = Channel<Unit>()
    
    fun openAddToPlaylistDialog(video: DataItem.Video) {
        _addToPlaylistEvent.value = video
    }
    
    fun dismissAddToPlaylistDialog() {
        _addToPlaylistEvent.value = null
    }
}

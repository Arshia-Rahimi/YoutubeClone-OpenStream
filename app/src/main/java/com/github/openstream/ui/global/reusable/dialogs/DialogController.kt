package com.github.openstream.ui.global.reusable.dialogs

import com.github.openstream.core.model.extractordata.DataItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object DialogController {
    private val _addToPlaylistEvent: MutableStateFlow<DataItem.Video?> = MutableStateFlow(null)
    val addToPlaylistEvent = _addToPlaylistEvent.asStateFlow()
    
    fun openAddToPlaylistDialog(video: DataItem.Video) {
        _addToPlaylistEvent.value = video
    }
    
    fun dismissAddToPlaylistDialog() {
        _addToPlaylistEvent.value = null
    }
}

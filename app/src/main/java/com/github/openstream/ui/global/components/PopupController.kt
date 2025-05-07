package com.github.openstream.ui.global.components

import com.github.openstream.core.model.extractordata.DataItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object PopupController {
    private val _showAddToPlaylistModal: MutableStateFlow<DataItem.Video?> = MutableStateFlow(null)
    val showAddToPlaylistModal = _showAddToPlaylistModal.asStateFlow()

    fun openAddToPlaylistDialog(video: DataItem.Video) {
        _showAddToPlaylistModal.value = video
    }
    
    fun dismissAddToPlaylistDialog() {
        _showAddToPlaylistModal.value = null
    }

    private val _showCreatePlaylistDialog = MutableStateFlow(false)
    val showCreatePlaylistDialog = _showCreatePlaylistDialog.asStateFlow()

    fun openCreatePlaylistDialog() {
        _showCreatePlaylistDialog.value = true
    }

    fun dismissCreatePlaylistDialog() {
        _showCreatePlaylistDialog.value = false
    }
}

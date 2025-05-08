package com.github.openstream.ui.global.components

import com.github.openstream.core.model.extractordata.DataItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object PopupController {
    private val _showSaveVideoToPlaylistsModal: MutableStateFlow<DataItem.Video?> =
        MutableStateFlow(null)
    val showSaveVideoToPlaylistsModal = _showSaveVideoToPlaylistsModal.asStateFlow()

    fun openSaveVideoToPlaylistModal(video: DataItem.Video) {
        _showSaveVideoToPlaylistsModal.value = video
    }

    fun dismissSaveVideoToPlaylistModal() {
        _showSaveVideoToPlaylistsModal.value = null
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

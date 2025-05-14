package com.github.openstream.ui.global.popups

import com.github.openstream.core.model.extractordata.VideoItem
import com.github.openstream.ui.global.popups.confirmationdialog.Confirmation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object PopupController {
    private val _showSaveVideoToPlaylistsModal: MutableStateFlow<VideoItem?> =
        MutableStateFlow(null)
    val showSaveVideoToPlaylistsModal = _showSaveVideoToPlaylistsModal.asStateFlow()

    fun openSaveVideoToPlaylistModal(video: VideoItem) {
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

    private val _showConfirmationDialog: MutableStateFlow<Confirmation?> =
        MutableStateFlow(null)
    val showConfirmationDialog = _showConfirmationDialog.asStateFlow()

    fun openConfirmationDialog(type: Confirmation) {
        _showConfirmationDialog.value = type
    }

    fun dismissConfirmationDialog() {
        _showConfirmationDialog.value = null
    }
}

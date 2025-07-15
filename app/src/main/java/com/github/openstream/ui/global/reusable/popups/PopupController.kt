package com.github.openstream.ui.global.reusable.popups

import com.github.openstream.core.shared.dataitem.VideoItem
import com.github.openstream.ui.global.reusable.popups.confirmationdialog.model.Confirmation
import com.github.openstream.ui.global.reusable.popups.inputdialog.model.InputType
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
    
    private val _showInputDialog = MutableStateFlow<InputType?>(null)
    val showInputDialog = _showInputDialog.asStateFlow()
    
    fun openInputDialog(input: InputType) {
        _showInputDialog.value = input
    }
    
    fun dismissInputDialog() {
        _showInputDialog.value = null
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
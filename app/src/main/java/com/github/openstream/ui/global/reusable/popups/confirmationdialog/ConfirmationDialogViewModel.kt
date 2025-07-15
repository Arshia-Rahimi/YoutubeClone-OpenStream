package com.github.openstream.ui.global.reusable.popups.confirmationdialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.openstream.core.common.compose.SnackBarController
import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.data.ChannelRepository
import com.github.openstream.core.data.PlaylistRepository
import com.github.openstream.ui.global.reusable.popups.PopupController
import com.github.openstream.ui.global.reusable.popups.confirmationdialog.model.Confirmation
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ConfirmationDialogViewModel(
    private val type: Confirmation,
    private val channelRepo: ChannelRepository,
    private val playlistRepo: PlaylistRepository,
) : ViewModel() {

    fun confirm() = when (type) {
        is Confirmation.UnsubscribeItem -> {
            channelRepo.unSubscribe(type.channel.id)
                .onEach {
                    when (it) {
                        is Resource.Loading -> Unit
                        is Resource.Error -> SnackBarController.sendEvent("failed to unsubscribe from ${type.channel.name}")
                        is Resource.Success -> {
                            SnackBarController.sendEvent("unsubscribed from ${type.channel.name}")
                            PopupController.dismissConfirmationDialog()
                        }
                    }
                }.launchIn(viewModelScope)
        }
        
        is Confirmation.DeletePlaylistItem -> {
            playlistRepo.deletePlaylist(type.playlist)
                .onEach {
                    when (it) {
                        is Resource.Loading -> Unit
                        is Resource.Error -> SnackBarController.sendEvent("failed to delete playlist ${type.playlist.name}")
                        is Resource.Success -> {
                            SnackBarController.sendEvent("deleted playlist ${type.playlist.name}")
                            PopupController.dismissConfirmationDialog()
                        }
                    }
                }.launchIn(viewModelScope)
        }
        
        is Confirmation.SavePlaylistItem -> {
            playlistRepo.savePlaylist(type.playlist)
                .onEach {
                    when (it) {
                        is Resource.Loading -> Unit
                        is Resource.Error -> SnackBarController.sendEvent("failed to save playlist ${type.playlist.name}")
                        is Resource.Success -> {
                            SnackBarController.sendEvent("saved playlist ${type.playlist.name}")
                            PopupController.dismissConfirmationDialog()
                        }
                    }
                }.launchIn(viewModelScope)
        }
    }

}

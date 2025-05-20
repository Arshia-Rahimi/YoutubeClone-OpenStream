package com.github.arshiarahimi.openstream.ui.global.popups.confirmationdialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.arshiarahimi.openstream.core.common.compose.SnackBarController
import com.github.arshiarahimi.openstream.core.common.util.Resource
import com.github.arshiarahimi.openstream.core.data.PlaylistRepository
import com.github.arshiarahimi.openstream.ui.global.popups.PopupController
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ConfirmationViewModel(
    private val type: Confirmation,
//    private val channelRepo: ChannelRepository,
    private val playlistRepo: PlaylistRepository,
) : ViewModel() {

    fun confirm() = when (type) {
        // todo add when local channels are available
        is UnsubscribeItem -> {
//                channelRepo.unSubscribe(type.channel.id)
//                    .onEach {
//                        when (it) {
//                            is Resource.Loading -> Unit
//                            is Resource.Error -> SnackBarController.sendEvent("failed to unsubscribe from ${type.channel.name}")
//                            is Resource.Success -> {
//                                SnackBarController.sendEvent("unsubscribed from ${type.channel.name}")
//                                PopupController.dismissConfirmationDialog()
//            }
//                        }
//                    }.launchIn(viewModelScope)
        }
        
        is DeletePlaylistItem -> {
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
        
        is SavePlaylistItem -> {
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

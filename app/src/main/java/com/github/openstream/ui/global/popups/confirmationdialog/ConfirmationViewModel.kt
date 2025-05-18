package com.github.openstream.ui.global.popups.confirmationdialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.openstream.core.common.compose.SnackBarController
import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.data.PlaylistRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ConfirmationViewModel(
    private val type: Confirmation,
//    private val channelRepo: ChannelRepository,
    private val playlistRepo: PlaylistRepository,
) : ViewModel() {
    
    // todo add undo to snackbar ## probably requires an UndoViewModel -needs more insight
    fun confirm() {
        when (type) {
            is UnsubscribeItem -> {
//                channelRepo.unSubscribe(type.channel.id)
//                    .onEach {
//                        when (it) {
//                            is Resource.Loading -> Unit
//                            is Resource.Error -> SnackBarController.sendEvent("failed to unsubscribe from ${type.channel.name}")
//                            is Resource.Success -> SnackBarController.sendEvent("unsubscribed from ${type.channel.name}")
//                        }
//                    }.launchIn(viewModelScope)
            }

            is Unsubscribe -> {
//                channelRepo.unSubscribe(type.channel.id)
//                    .onEach {
//                        when (it) {
//                            is Resource.Loading -> Unit
//                            is Resource.Error -> SnackBarController.sendEvent("failed to unsubscribe from ${type.channel.metadata.name}")
//                            is Resource.Success -> SnackBarController.sendEvent("unsubscribed from ${type.channel.metadata.name}")
//                        }
//                    }.launchIn(viewModelScope)
            }

            is DeletePlaylistItem -> {
                playlistRepo.deletePlaylist(type.playlist)
                    .onEach {
                        when (it) {
                            is Resource.Loading -> Unit
                            is Resource.Error -> SnackBarController.sendEvent("failed to delete playlist ${type.playlist.name}")
                            is Resource.Success -> SnackBarController.sendEvent("deleted playlist ${type.playlist.name}")
                        }
                    }.launchIn(viewModelScope)
            }
        }
    }
}

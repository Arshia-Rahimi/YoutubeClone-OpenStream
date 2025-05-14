package com.github.openstream.ui.global.popups.confirmationdialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.openstream.core.common.compose.SnackBarController
import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.data.ChannelRepository
import com.github.openstream.core.data.PlaylistRepository
import kotlinx.coroutines.launch

class ConfirmationViewModel(
    private val type: Confirmation,
    private val channelRepo: ChannelRepository,
    private val playlistRepo: PlaylistRepository,
) : ViewModel() {

    // todo add undo to snackbar
    fun confirm() {
        viewModelScope.launch {
            when (type) {
                is UnsubscribeItem -> {
                    channelRepo.unSubscribe(type.channel.id)
                        .collect {
                            when (it) {
                                is Resource.Loading -> Unit
                                is Resource.Error -> SnackBarController.sendEvent("failed to unsubscribe from ${type.channel.name}")
                                is Resource.Success -> SnackBarController.sendEvent("unsubscribed from ${type.channel.name}")
                            }
                        }
                }

                is Unsubscribe -> {
                    channelRepo.unSubscribe(type.channel.metadata.id)
                        .collect {
                            when (it) {
                                is Resource.Loading -> Unit
                                is Resource.Error -> SnackBarController.sendEvent("failed to unsubscribe from ${type.channel.metadata.name}")
                                is Resource.Success -> SnackBarController.sendEvent("unsubscribed from ${type.channel.metadata.name}")
                            }
                        }
                }

                is DeletePlaylistItem -> {
                    playlistRepo.deletePlaylist(type.playlist)
                        .collect {
                            when (it) {
                                is Resource.Loading -> Unit
                                is Resource.Error -> SnackBarController.sendEvent("failed to delete playlist ${type.playlist.name}")
                                is Resource.Success -> SnackBarController.sendEvent("deleted playlist ${type.playlist.name}")
                            }
                        }
                }

                is DeletePlaylist -> {
                    playlistRepo.deletePlaylist(type.playlist)
                        .collect {
                            when (it) {
                                is Resource.Loading -> Unit
                                is Resource.Error -> SnackBarController.sendEvent("failed to delete playlist ${type.playlist.metadata.name}")
                                is Resource.Success -> SnackBarController.sendEvent("deleted playlist ${type.playlist.metadata.name}")
                            }
                        }
                }
            }
        }
    }
}

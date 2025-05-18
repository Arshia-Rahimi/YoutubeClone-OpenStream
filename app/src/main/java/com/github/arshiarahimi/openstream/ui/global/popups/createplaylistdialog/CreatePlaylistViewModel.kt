package com.github.arshiarahimi.openstream.ui.global.popups.createplaylistdialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.arshiarahimi.openstream.core.common.compose.SnackBarController
import com.github.arshiarahimi.openstream.core.common.util.Resource
import com.github.arshiarahimi.openstream.core.data.PlaylistRepository
import com.github.arshiarahimi.openstream.ui.global.popups.PopupController
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(
    private val playlistRepo: PlaylistRepository,
) : ViewModel() {
    fun createPlaylist(title: String) {
        viewModelScope.launch {
            playlistRepo.createPlaylist(title).collect {
                when (it) {
                    is Resource.Error -> SnackBarController
                        .sendEvent("failed to create playlist: $title")

                    is Resource.Success -> {
                        SnackBarController
                            .sendEvent("created playlist: $title")
                        PopupController.dismissCreatePlaylistDialog()
                    }

                    else -> Unit
                }
            }
        }
    }
}

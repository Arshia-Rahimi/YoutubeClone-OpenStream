package com.github.openstream.ui.global.components.createplaylistdialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.openstream.core.common.compose.SnackBarController
import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.data.PlaylistRepository
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

                    is Resource.Success -> SnackBarController
                        .sendEvent("created playlist: $title")

                    else -> Unit
                }
            }
        }
    }
}

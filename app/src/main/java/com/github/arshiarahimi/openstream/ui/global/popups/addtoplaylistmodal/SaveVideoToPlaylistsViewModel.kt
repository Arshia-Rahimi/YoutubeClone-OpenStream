package com.github.arshiarahimi.openstream.ui.global.popups.addtoplaylistmodal

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.arshiarahimi.openstream.core.common.compose.SnackBarController
import com.github.arshiarahimi.openstream.core.common.util.Resource
import com.github.arshiarahimi.openstream.core.data.PlaylistRepository
import com.github.arshiarahimi.openstream.core.model.dataitem.PlaylistItem
import com.github.arshiarahimi.openstream.core.model.dataitem.VideoItem
import com.github.arshiarahimi.openstream.ui.global.popups.PopupController
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SaveVideoToPlaylistsViewModel(
    private val video: VideoItem,
    private val playlistRepo: PlaylistRepository,
) : ViewModel() {

    val localPlaylists = mutableStateMapOf<PlaylistItem.LocalOnlyPlaylistItem, Boolean>()
        .apply {
            playlistRepo.playlists
                .onEach {
                    val currentPlaylists = mapKeys { it.key.id }
                    clear()
                    it.filterIsInstance<PlaylistItem.LocalOnlyPlaylistItem>()
                        .sortedBy { it.id }
                        .forEach { playlist ->
                            put(playlist, currentPlaylists[playlist.id] == true)
                        }
                }.launchIn(viewModelScope)
        }

    fun saveVideoToPlaylists() {
        viewModelScope.launch {
            playlistRepo.saveVideoToPlaylists(video, localPlaylists)
                .collect {
                    when (it) {
                        is Resource.Error -> SnackBarController.sendEvent(
                            it.message ?: "failed to save to playlist"
                        )

                        is Resource.Success -> {
                            SnackBarController.sendEvent("saved to playlist")
                            PopupController.dismissSaveVideoToPlaylistModal()
                        }

                        else -> Unit
                    }
                }
        }
    }

    fun showCreatePlaylistDialog() = PopupController.openCreatePlaylistDialog()
}

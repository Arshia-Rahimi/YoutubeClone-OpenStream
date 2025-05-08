package com.github.openstream.ui.global.popups.addtoplaylistmodal

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.openstream.core.common.compose.SnackBarController
import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.data.PlaylistRepository
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.ui.global.popups.PopupController
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SaveVideoToPlaylistsViewModel(
    private val video: DataItem.Video,
    private val playlistRepo: PlaylistRepository,
) : ViewModel() {

    val localPlaylists = mutableStateMapOf<DataItem.Playlist.LocalOnlyPlaylist, Boolean>()
        .apply {
            playlistRepo.playlists
                .onEach {
                    val currentPlaylists = mapKeys { it.key.id }
                    clear()
                    it.filter { it is DataItem.Playlist.LocalOnlyPlaylist }
                        .map { it as DataItem.Playlist.LocalOnlyPlaylist }
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

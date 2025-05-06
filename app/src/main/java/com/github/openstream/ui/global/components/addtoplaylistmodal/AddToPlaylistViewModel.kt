package com.github.openstream.ui.global.components.addtoplaylistmodal

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.openstream.core.common.compose.SnackBarController
import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.common.util.sendPulse
import com.github.openstream.core.data.PlaylistRepository
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.ui.global.components.PopupController
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AddToPlaylistViewModel(
    private val video: DataItem.Video,
    private val playlistRepo: PlaylistRepository,
) : ViewModel() {

    val localPlaylists = mutableStateMapOf<DataItem.Playlist.LocalPlaylist, Boolean>()

    init {
        playlistRepo.playlists
            .onEach {
                it.filter { it is DataItem.Playlist.LocalPlaylist }
                    .map { it as DataItem.Playlist.LocalPlaylist }
                    .forEach { playlist ->
                        if (playlist !in localPlaylists.keys) {
                            localPlaylists.put(playlist, false)
                        }
                    }
                val playlists = localPlaylists.toMap()
                localPlaylists.clear()
                localPlaylists.putAll(playlists)
            }.launchIn(viewModelScope)
    }

    fun syncVideoInPlaylists() {
        viewModelScope.launch {
            playlistRepo.syncVideoPlaylists(video, localPlaylists)
                .collect {
                    when(it) {
                        is Resource.Error -> SnackBarController.sendEvent(it.message ?: "failed to save to playlist")
                        is Resource.Success -> SnackBarController.sendEvent("saved to playlists")
                        else -> Unit
                    }
                }
        }
    }

    fun showCreatePlaylistDialog() = PopupController.createPlaylistDialogEvent.sendPulse()
}

package com.github.openstream.ui.global.reusable.dialogs.addtoplaylist

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.openstream.core.common.compose.SnackBarController
import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.data.PlaylistRepository
import com.github.openstream.core.model.extractordata.DataItem
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.core.component.getScopeName
import org.schabi.newpipe.extractor.timeago.patterns.vi

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

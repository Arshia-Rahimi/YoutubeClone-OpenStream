package com.github.freetube.ui.global.playlist

import androidx.compose.runtime.mutableStateListOf
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.github.freetube.core.common.util.Resource
import com.github.freetube.core.data.PlaylistRepository
import com.github.freetube.core.extractor.model.DataItem
import com.github.freetube.core.extractor.playlist.PlaylistResult
import com.github.freetube.core.extractor.playlist.PlaylistUnit
import kotlinx.coroutines.launch

class PlaylistScreenModel(
    private val url: String,
    private val playlistRepository: PlaylistRepository,
) : StateScreenModel<PlaylistScreenModel.UiState>(UiState.Loading) {

    sealed interface UiState {
        data object Loading : UiState
        data class Error(val message: String? = null) : UiState
        data class Success(val playlistResult: PlaylistResult) : UiState
    }

    private lateinit var playlist: PlaylistUnit
    val items = mutableStateListOf<DataItem>()

    init {
        screenModelScope.launch {
            playlistRepository.getPlaylist(url)
                .collect {
                    mutableState.value = when (it) {
                        is Resource.Loading -> UiState.Loading
                        is Resource.Error -> UiState.Error(it.message)
                        is Resource.Success -> {
                            playlist = it.data
                            items += it.data.firstPage.items
                            UiState.Success(it.data.firstPage)
                        }
                    }
                }
        }
    }


    fun getNextPage() {
        screenModelScope.launch {
            if (playlist.nextPage == null) return@launch
            playlistRepository.getNextPage(playlist)
                .collect {
                    when (it) {
                        is Resource.Loading -> {}
                        is Resource.Error -> {}
                        is Resource.Success -> {
                            it.data?.let { items += it }
                        }
                    }
                }
        }
    }
}

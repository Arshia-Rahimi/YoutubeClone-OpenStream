package com.github.freetube.ui.global.player

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.github.freetube.core.common.util.Resource
import com.github.freetube.core.data.VideoRepository
import com.github.freetube.core.extractor.video.VideoData
import com.github.freetube.core.media3.LibreTubeMediaPlayer
import kotlinx.coroutines.launch

class PlayerScreenModel(
    private val player: LibreTubeMediaPlayer,
    private val videoRepository: VideoRepository,
) : StateScreenModel<PlayerScreenModel.UiState>(UiState.Loading) {

    sealed interface UiState {
        data object Loading : UiState
        data class Error(val message: String? = null) : UiState
        data class Success(val data: VideoData) : UiState
    }

    private fun getVideo(url: String) {
        screenModelScope.launch {
            videoRepository.fetchVideo(url)
                .collect {
                    mutableState.value = when (it) {
                        is Resource.Loading -> UiState.Loading
                        is Resource.Error -> UiState.Error(it.message)
                        is Resource.Success -> {
                            // todo start player
                            UiState.Success(it.data)
                        }
                    }
                }
        }
    }
}

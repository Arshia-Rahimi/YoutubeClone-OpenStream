package com.github.openstream.ui.feature.downloads

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.openstream.core.data.QueueRepository
import com.github.openstream.core.model.dataitem.VideoItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class QueueViewModel(
    private val queueRepo: QueueRepository,
) : ViewModel() {
    
    val queue = mutableStateListOf<VideoItem>()
        .apply {
            queueRepo.queue.onEach {
                clear()
                addAll(it)
            }.launchIn(viewModelScope)
        }
    
    val currentVideo =
        queueRepo.currentVideo.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    
    fun playFrom(videoIndex: Int) {
        viewModelScope.launch {
            queueRepo.playFrom(videoIndex)
        }
    }
}

package com.github.arshiarahimi.openstream.ui.feature.downloads

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.arshiarahimi.openstream.core.data.QueueRepository
import com.github.arshiarahimi.openstream.core.model.dataitem.VideoItem
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class QueueViewModel(
    private val queueRepo: QueueRepository,
) : ViewModel() {
    val queue = mutableStateListOf<VideoItem>()
        .apply {
            queueRepo.getQueue().onEach {
                clear()
                addAll(it)
            }.launchIn(viewModelScope)
        }

    fun updateQueue(newQueue: List<VideoItem>) {
        viewModelScope.launch {
            queueRepo.updateQueue(newQueue)
        }
    }
}

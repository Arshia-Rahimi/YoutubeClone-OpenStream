package com.github.openstream.ui.feature.settings.root

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.openstream.core.common.compose.SnackBarController
import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.data.CacheRepository
import com.github.openstream.core.data.VideoRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SettingsViewModel(
    private val videoRepo: VideoRepository,
    private val cacheRepo: CacheRepository,
) : ViewModel() {

    var localVideoHistoryLoading by mutableStateOf(false)
    var diskImageCacheLoading by mutableStateOf(false)

    fun clearLocalVideoHistory() {
        videoRepo.deleteLocalVideoHistory().onEach {
            when (it) {
                is Resource.Success -> {
                    localVideoHistoryLoading = false
                    SnackBarController.sendEvent("cleared local video history")
                }

                is Resource.Error -> {
                    localVideoHistoryLoading = false
                    SnackBarController.sendEvent("failed to clear local video history")
                }

                else -> localVideoHistoryLoading = true
            }
        }.launchIn(viewModelScope)
    }

    fun clearDiskImageCache() {
        cacheRepo.clearDiskImageCache().onEach {
            when (it) {
                is Resource.Success -> {
                    diskImageCacheLoading = false
                    SnackBarController.sendEvent("cleared disk image cache")
                }

                is Resource.Error -> {
                    diskImageCacheLoading = false
                    SnackBarController.sendEvent("failed to clear disk image cache")
                }

                else -> diskImageCacheLoading = true
            }
        }.launchIn(viewModelScope)
    }
    
}

package com.github.arshiarahimi.openstream.ui.feature.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.arshiarahimi.openstream.core.common.compose.SnackBarController
import com.github.arshiarahimi.openstream.core.common.util.Resource
import com.github.arshiarahimi.openstream.core.data.VideoRepository
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val videoRepo: VideoRepository,
) : ViewModel() {

    var localVideoHistoryLoading by mutableStateOf(false)

    fun clearLocalVideoHistory() {
        viewModelScope.launch {
            videoRepo.deleteLocalVideoHistory().collect {
                when (it) {
                    is Resource.Success -> {
                        localVideoHistoryLoading = false
                        SnackBarController.sendEvent("clear local video history")
                    }

                    is Resource.Error -> {
                        localVideoHistoryLoading = false
                        SnackBarController.sendEvent("failed to clear local video history")
                    }

                    else -> localVideoHistoryLoading = true
                }
            }
        }
    }
}

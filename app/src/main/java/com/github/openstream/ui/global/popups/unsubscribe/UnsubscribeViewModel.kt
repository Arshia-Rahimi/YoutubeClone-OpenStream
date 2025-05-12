package com.github.openstream.ui.global.popups.unsubscribe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.openstream.core.common.compose.SnackBarController
import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.data.ChannelRepository
import kotlinx.coroutines.launch

class UnsubscribeViewModel(
    private val channelId: Long,
    private val name: String,
    private val channelRepo: ChannelRepository,
) : ViewModel() {
    fun unsubscribe() {
        viewModelScope.launch {
            channelRepo.unSubscribe(channelId)
                .collect {
                    when (it) {
                        is Resource.Loading -> Unit
                        is Resource.Error -> SnackBarController.sendEvent("failed to unsubscribe from $name")
                        is Resource.Success -> SnackBarController.sendEvent("unsubscribed from $name")
                    }
                }
        }
    }
}

package com.github.openstream.ui.feature.settings.root

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.openstream.core.common.compose.SnackBarController
import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.data.CacheRepository
import com.github.openstream.core.data.PreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val cacheRepo: CacheRepository,
    private val preferencesRepo: PreferencesRepository,
) : ViewModel() {

    var localVideoHistoryLoading by mutableStateOf(false)
    var clearCacheLoading by mutableStateOf(false)
    var clearWatchHistoryLoading by mutableStateOf(false)
    var clearLogLoading by mutableStateOf(false)
    var clearCookiesLoading by mutableStateOf(false)

    val cookies = preferencesRepo.preferences
        .map { it.cookies }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun clearLocalVideoHistory() {
        cacheRepo.deleteLocalVideoHistory().onEach {
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

    fun clearCache() {
        cacheRepo.clearAllCache().onEach {
            when (it) {
                is Resource.Success -> {
                    clearCacheLoading = false
                    SnackBarController.sendEvent("cleared all cache")
                }

                is Resource.Error -> {
                    clearCacheLoading = false
                    SnackBarController.sendEvent("failed to clear cache")
                }

                else -> clearCacheLoading = true
            }
        }.launchIn(viewModelScope)
    }

    fun clearWatchHistory() {
        cacheRepo.clearWatchHistory().onEach {
            when (it) {
                is Resource.Success -> {
                    clearWatchHistoryLoading = false
                    SnackBarController.sendEvent("cleared watch history")
                }

                is Resource.Error -> {
                    clearWatchHistoryLoading = false
                    SnackBarController.sendEvent("failed to clear watch history")
                }

                else -> clearWatchHistoryLoading = true
            }
        }.launchIn(viewModelScope)
    }

    fun clearLog() {
        cacheRepo.clearLog().onEach {
            when (it) {
                is Resource.Success -> {
                    clearLogLoading = false
                    SnackBarController.sendEvent("cleared log")
                }

                is Resource.Error -> {
                    clearLogLoading = false
                    SnackBarController.sendEvent("failed to clear log")
                }

                else -> clearLogLoading = true
            }
        }.launchIn(viewModelScope)
    }

    fun clearCookies() {
        viewModelScope.launch {
            clearCookiesLoading = true
            preferencesRepo.setCookies(null)
            clearCookiesLoading = false
        }
    }
}

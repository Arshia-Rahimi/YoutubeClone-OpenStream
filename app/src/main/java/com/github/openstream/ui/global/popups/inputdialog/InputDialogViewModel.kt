package com.github.openstream.ui.global.popups.inputdialog

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.openstream.core.common.compose.SnackBarController
import com.github.openstream.core.common.util.Resource
import com.github.openstream.core.data.PlaylistRepository
import com.github.openstream.ui.global.popups.PopupController
import com.github.openstream.ui.global.popups.inputdialog.model.CreatePlaylist
import com.github.openstream.ui.global.popups.inputdialog.model.InputType
import com.github.openstream.ui.global.popups.inputdialog.model.SearchPlaylist
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class InputDialogViewModel(
    private val type: InputType,
    private val playlistRepo: PlaylistRepository,
) : ViewModel() {
    val input = mutableStateOf("")
    val isLoading = mutableStateOf(false)
    
    fun confirm() {
        when (type) {
            is CreatePlaylist -> playlistRepo.createPlaylist(input.value).onEach {
                when (it) {
                    is Resource.Error -> {
                        isLoading.value = false
                        SnackBarController.sendEvent("failed to create playlist: ${input.value}")
                    }
                    
                    is Resource.Success -> {
                        isLoading.value = false
                        input.value = ""
                        SnackBarController.sendEvent("created playlist: ${input.value}")
                        PopupController.dismissInputDialog()
                    }
                    
                    is Resource.Loading -> isLoading.value = true
                }
            }.launchIn(viewModelScope)
            
            is SearchPlaylist -> playlistRepo.getPlaylist(input.value).onEach { fetchedPlaylist ->
                when (fetchedPlaylist) {
                    is Resource.Error -> {
                        isLoading.value = false
                        SnackBarController.sendEvent("failed to find playlist")
                    }
                    
                    is Resource.Loading -> isLoading.value = true
                    is Resource.Success -> {
                        playlistRepo.savePlaylist(fetchedPlaylist.data).collect {
                            when (it) {
                                is Resource.Success -> {
                                    SnackBarController.sendEvent("saved playlist ${fetchedPlaylist.data.name}")
                                    isLoading.value = false
                                    input.value = ""
                                    PopupController.dismissInputDialog()
                                }
                                
                                is Resource.Error -> {
                                    isLoading.value = false
                                    SnackBarController.sendEvent("failed to find playlist")
                                }
                                
                                is Resource.Loading -> Unit
                            }
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
    
}

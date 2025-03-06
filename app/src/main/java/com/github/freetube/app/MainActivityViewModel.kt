package com.github.freetube.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.freetube.core.data.LibreTubeDataRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class MainActivityViewModel(
    libreTubeData: LibreTubeDataRepository,
): ViewModel() {
    
    val libreTubeData = libreTubeData.data
        .stateIn(
            scope = viewModelScope,
            initialValue = null,
            started = SharingStarted.WhileSubscribed(5000L)
        )
    
}

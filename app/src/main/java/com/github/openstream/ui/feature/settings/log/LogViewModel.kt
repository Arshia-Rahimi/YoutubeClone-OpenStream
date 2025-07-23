package com.github.openstream.ui.feature.settings.log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.openstream.core.common.util.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LogViewModel(
    logger: Logger,
) : ViewModel() {
    
    private val _log = MutableStateFlow("")
    val log = _log.asStateFlow()
    
    init {
        logger.logStream.onEach {
            _log.value = _log.value + it
        }.launchIn(viewModelScope)
    }
    
}

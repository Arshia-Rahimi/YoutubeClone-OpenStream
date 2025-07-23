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
        .apply {
            logger.logStream.onEach { newLine ->
                value = value + newLine
            }.launchIn(viewModelScope)
        }
    val log = _log.asStateFlow()
    
}

package com.github.openstream.ui.feature.settings.log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.openstream.core.common.util.Logger
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class LogViewModel(
    logger: Logger,
) : ViewModel() {
    
    val log = logger.logStream.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")
    
}

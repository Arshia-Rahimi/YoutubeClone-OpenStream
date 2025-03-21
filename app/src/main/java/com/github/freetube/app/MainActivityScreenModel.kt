package com.github.freetube.app

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.github.freetube.core.common.util.onFirst
import com.github.freetube.core.data.LibreTubeDataRepository
import com.github.freetube.core.extractor.OkHttpDownloader
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.schabi.newpipe.extractor.NewPipe
import org.schabi.newpipe.extractor.localization.Localization

class MainActivityScreenModel(
    private val downloader: OkHttpDownloader,
    libreTubeData: LibreTubeDataRepository,
) : ScreenModel {
    
    val libreTubeData = libreTubeData.data
        .onFirst { loadNewPipeConfig() }
        .stateIn(
            scope = screenModelScope,
            initialValue = null,
            started = SharingStarted.WhileSubscribed(5000L)
        )
    
    private fun loadNewPipeConfig() =
        // todo get locale and add it to dataStore
        NewPipe.init(downloader, Localization("en", "US"))
}

package com.github.openstream.core.extractor

import com.github.openstream.core.data.PreferencesRepository
import kotlinx.coroutines.flow.map
import org.schabi.newpipe.extractor.downloader.Downloader
import org.schabi.newpipe.extractor.downloader.Request
import org.schabi.newpipe.extractor.downloader.Response


class KtorDownloader(
    private val preferencesRepo: PreferencesRepository,
) : Downloader() {
    
    private val customCookies = preferencesRepo.preferences
        .map { it.cookies }
    
    override fun execute(request: Request): Response? {
        TODO("Not yet implemented")
    }
    
}

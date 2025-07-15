package com.github.openstream.core.shared.extractor.data

import com.github.openstream.core.shared.dataitem.DataItem
import org.schabi.newpipe.extractor.Page
import org.schabi.newpipe.extractor.services.youtube.extractors.YoutubeSearchExtractor

data class SearchResult(
    val extractor: YoutubeSearchExtractor,
    val items: List<DataItem>,
    var nextPage: Page?
)

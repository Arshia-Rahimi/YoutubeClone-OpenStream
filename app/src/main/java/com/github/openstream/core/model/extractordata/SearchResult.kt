package com.github.openstream.core.model.extractordata

import com.github.openstream.core.model.dataitem.DataItem
import org.schabi.newpipe.extractor.Page
import org.schabi.newpipe.extractor.services.youtube.extractors.YoutubeSearchExtractor

data class SearchResult(
    val extractor: YoutubeSearchExtractor,
    val items: List<DataItem>,
    var nextPage: Page?
)

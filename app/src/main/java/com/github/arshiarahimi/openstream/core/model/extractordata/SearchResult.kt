package com.github.arshiarahimi.openstream.core.model.extractordata

import org.schabi.newpipe.extractor.Page
import org.schabi.newpipe.extractor.services.youtube.extractors.YoutubeSearchExtractor

data class SearchResult(
    val extractor: YoutubeSearchExtractor,
    val items: List<DataItem>,
    var nextPage: Page?
)

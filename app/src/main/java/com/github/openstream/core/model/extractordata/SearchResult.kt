package com.github.openstream.core.model.extractordata

import androidx.compose.runtime.snapshots.SnapshotStateList
import org.schabi.newpipe.extractor.Page
import org.schabi.newpipe.extractor.services.youtube.extractors.YoutubeSearchExtractor

data class SearchResult(
    val extractor: YoutubeSearchExtractor,
    // todo
//    val searchSuggestion: String,
//    val isCorrectedSearch: Boolean,
    val items: List<DataItem>,
    var nextPage: Page?
)

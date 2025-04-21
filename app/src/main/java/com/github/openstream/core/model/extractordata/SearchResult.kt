package com.github.openstream.core.model.extractordata

import androidx.compose.runtime.snapshots.SnapshotStateList
import org.schabi.newpipe.extractor.Page
import org.schabi.newpipe.extractor.services.youtube.extractors.YoutubeSearchExtractor

data class SearchResult(
    val extractor: YoutubeSearchExtractor,
    val searchSuggestion: String,
    val isCorrectedSearch: Boolean,
    val items: SnapshotStateList<DataItem>,
    var nextPage: Page?
)

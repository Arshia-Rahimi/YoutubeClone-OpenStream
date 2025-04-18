package com.github.openstream.core.extractor.search

import com.github.openstream.core.extractor.model.DataItem

data class InitialSearchResult(
    val searchSuggestion: String,
    val isCorrectedSearch: Boolean,
    val firstPage: List<DataItem>,
)

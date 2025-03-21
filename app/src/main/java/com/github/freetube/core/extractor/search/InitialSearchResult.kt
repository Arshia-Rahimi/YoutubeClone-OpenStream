package com.github.freetube.core.extractor.search

import com.github.freetube.core.extractor.model.DataItem

data class InitialSearchResult(
    val searchSuggestion: String,
    val isCorrectedSearch: Boolean,
    val firstPage: List<DataItem>,
)

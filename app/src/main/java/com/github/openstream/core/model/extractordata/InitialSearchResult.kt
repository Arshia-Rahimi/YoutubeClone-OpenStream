package com.github.openstream.core.model.extractordata

data class InitialSearchResult(
    val searchSuggestion: String,
    val isCorrectedSearch: Boolean,
    val firstPage: List<DataItem>,
)

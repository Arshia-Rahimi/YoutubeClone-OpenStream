package com.github.freetube.core.extractor

data class InitialSearchResult(
    val searchSuggestion: String,
    val isCorrectedSearch: Boolean,
    val firstPage: List<DataItem>,
)

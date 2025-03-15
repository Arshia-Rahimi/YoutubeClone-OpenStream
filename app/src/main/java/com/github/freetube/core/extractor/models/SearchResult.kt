package com.github.freetube.core.extractor.models

data class SearchResult(
    val url: String,
    val searchSuggestion: String? = null,
    val isCorrectedSearch: Boolean,
)

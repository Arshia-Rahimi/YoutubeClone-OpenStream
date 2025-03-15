package com.github.freetube.core.extractor

import com.github.freetube.core.common.youtubeService
import com.github.freetube.core.extractor.models.Page
import kotlin.properties.Delegates

class SearchUnit(
    query: String,
    contentFilter: List<String>? = null,
    sortFilter: String? = null,
) {
    private val extractor = youtubeService
        .getSearchExtractor(query, contentFilter, sortFilter)
    
    lateinit var url: String
    lateinit var searchSuggestion: String
    var isCorrectedSearch by Delegates.notNull<Boolean>()
    val resultPages = emptyList<Page>()
//    private val nextPage = 
    
//    init {
//        extractor.fetchPage()
//        val initialPage = extractor.initialPage
//        resultPages += Page(initialPage.items, initialPage.hasNextPage(), initialPage.nextPage)
//    }
}

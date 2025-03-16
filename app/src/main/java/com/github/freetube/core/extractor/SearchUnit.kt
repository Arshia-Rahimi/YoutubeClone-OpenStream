package com.github.freetube.core.extractor

import com.github.freetube.core.common.youtubeService
import org.schabi.newpipe.extractor.InfoItem
import org.schabi.newpipe.extractor.ListExtractor
import org.schabi.newpipe.extractor.Page

class SearchUnit(
    query: String,
    contentFilter: List<String>? = null,
    sortFilter: String? = null,
) {
    val url: String
    val searchSuggestion: String
    val isCorrectedSearch: Boolean
    val result = mutableListOf<List<DataItem>>()
    var hasNextPage: Boolean
        private set
    
    private var nextPage: Page? = null
    private val extractor = youtubeService
        .getSearchExtractor(query, contentFilter, sortFilter)

    init {
        extractor.fetchPage()
        url = extractor.url
        searchSuggestion = extractor.searchSuggestion
        isCorrectedSearch = extractor.isCorrectedSearch
        val firstPage = extractor.initialPage
        firstPage.items.appendToResult()
        hasNextPage = firstPage.hasNextPage()
        nextPage = firstPage.nextPage
    }

    fun fetchNextPage() {
        nextPage?.let {
            val currentPage = extractor.getPage(it)
            currentPage.items.appendToResult()
            updateNextPageInfo(currentPage)
        }
    }
    
    private fun List<InfoItem>.appendToResult() { result += toList() }
    
    private fun updateNextPageInfo(currentPage: ListExtractor.InfoItemsPage<InfoItem>) {
        hasNextPage = currentPage.hasNextPage()
        nextPage = currentPage.nextPage
    }
}

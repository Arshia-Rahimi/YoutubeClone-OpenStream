package com.github.openstream.core.extractor.search

import com.github.openstream.core.extractor.YtService
import com.github.openstream.core.extractor.model.DataItem
import com.github.openstream.core.extractor.model.toList
import org.schabi.newpipe.extractor.Page

class SearchUnit(
    query: String,
    contentFilter: List<String>,
    sortFilter: String?,
) {
    var nextPage: Page?
    private val extractor = YtService.getSearchExtractor(query, contentFilter, sortFilter)
    val firstPage: InitialSearchResult
    
    init {
        extractor.fetchPage()
        firstPage = InitialSearchResult(
            searchSuggestion = extractor.searchSuggestion,
            isCorrectedSearch = extractor.isCorrectedSearch,
            firstPage = extractor.initialPage.items.toList(),
        )
        nextPage = extractor.initialPage.nextPage
    }

    fun fetchNextPage(): List<DataItem>? =
        nextPage?.let {
            val currentPage = extractor.getPage(nextPage)
            nextPage = currentPage.nextPage
            return currentPage.items.toList()
        }
}

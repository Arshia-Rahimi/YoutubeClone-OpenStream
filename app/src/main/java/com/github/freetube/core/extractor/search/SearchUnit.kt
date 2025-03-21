package com.github.freetube.core.extractor.search

import com.github.freetube.core.common.youtubeService
import com.github.freetube.core.extractor.model.DataItem
import com.github.freetube.core.extractor.model.toList
import org.schabi.newpipe.extractor.Page

class SearchUnit(
    query: String,
    contentFilter: List<String>,
    sortFilter: String?,
) {
    private var nextPage: Page?
    private val extractor = youtubeService.getSearchExtractor(query, contentFilter, sortFilter)
    
    init {
        extractor.fetchPage()
        nextPage = extractor.initialPage.nextPage
    }
    
    fun getFirstPage() =
        InitialSearchResult(
            searchSuggestion = extractor.searchSuggestion,
            isCorrectedSearch = extractor.isCorrectedSearch,
            firstPage = extractor.initialPage.items.toList(),
        )
    
    fun getNextPage(): List<DataItem>? =
        nextPage?.let {
            val currentPage = extractor.getPage(nextPage)
            nextPage = currentPage.nextPage
            return currentPage.items.toList()
        }
}

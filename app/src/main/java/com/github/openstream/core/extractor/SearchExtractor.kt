package com.github.openstream.core.extractor

import com.github.openstream.core.extractor.util.YtService
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.core.model.extractordata.InitialSearchResult
import com.github.openstream.core.model.extractordata.toList
import org.schabi.newpipe.extractor.Page

class SearchExtractor(
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
package com.github.openstream.core.extractor

import com.github.openstream.core.extractor.util.YtService
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.core.model.extractordata.SearchResult
import com.github.openstream.core.model.extractordata.toListOfDataItem
import org.schabi.newpipe.extractor.services.youtube.extractors.YoutubeSearchExtractor

object SearchExtractor {
    
    fun fetchSearchResult(
        query: String,
        contentFilter: List<String>,
        sortFilter: String?,
    ): SearchResult {
        val extractor = YtService.getSearchExtractor(query, contentFilter, sortFilter) as YoutubeSearchExtractor
        extractor.fetchPage()
        return SearchResult(
            extractor = extractor,
            searchSuggestion = extractor.searchSuggestion,
            isCorrectedSearch = extractor.isCorrectedSearch,
            items = extractor.initialPage.items.toListOfDataItem(),
            nextPage = extractor.initialPage.nextPage,
        )
    }
    
    fun fetchNextPage(search: SearchResult): List<DataItem> =
        search.nextPage?.let {
            val currentPage = search.extractor.getPage(it)
            search.nextPage = currentPage.nextPage
            currentPage.items.toListOfDataItem()
        } ?: emptyList()
}

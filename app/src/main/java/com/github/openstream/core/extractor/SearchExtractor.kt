package com.github.openstream.core.extractor

import androidx.compose.runtime.toMutableStateList
import com.github.openstream.core.extractor.util.YtService
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.core.model.extractordata.SearchResult
import com.github.openstream.core.model.extractordata.toList
import org.schabi.newpipe.extractor.Page
import org.schabi.newpipe.extractor.services.youtube.extractors.YoutubePlaylistExtractor
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
            items = extractor.initialPage.items.toList().toMutableStateList(),
            nextPage = extractor.initialPage.nextPage,
        )
    }
    
    fun fetchNextPage(search: SearchResult) {
        search.nextPage?.let {
            val currentPage = search.extractor.getPage(it)
            search.nextPage = currentPage.nextPage
            search.items.addAll(currentPage.items.toList())
        }
    }
}

package com.github.arshiarahimi.openstream.core.extractor.datasource

import com.github.arshiarahimi.openstream.core.extractor.YtService
import com.github.arshiarahimi.openstream.core.model.extractordata.DataItem
import com.github.arshiarahimi.openstream.core.model.extractordata.SearchResult
import com.github.arshiarahimi.openstream.core.model.extractordata.toListOfDataItem
import org.schabi.newpipe.extractor.services.youtube.extractors.YoutubeSearchExtractor

object SearchRemoteDataSource {

    fun fetchSearchResult(
        query: String,
        contentFilter: List<String>,
        sortFilter: String?,
    ): SearchResult {
        val extractor =
            YtService.getSearchExtractor(query, contentFilter, sortFilter) as YoutubeSearchExtractor
        extractor.fetchPage()
        return SearchResult(
            extractor = extractor,
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

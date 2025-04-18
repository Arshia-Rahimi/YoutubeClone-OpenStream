package com.github.openstream.ui.feature.search

sealed interface SearchAction {
    data object OnSearch : SearchAction
    data object OnNextPage : SearchAction
}

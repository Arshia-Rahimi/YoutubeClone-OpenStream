package com.github.freetube.ui.feature.search

sealed interface SearchAction {
    data object OnSearch : SearchAction
    data object OnNextPage : SearchAction
}

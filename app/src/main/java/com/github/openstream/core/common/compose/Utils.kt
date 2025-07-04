package com.github.openstream.core.common.compose

import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun <T> Flow<List<T>>.collectToSnapShotStateList(
    scope: CoroutineScope,
    sort: (List<T>) -> List<T> = { it },
) =
    mutableStateListOf<T>().apply {
        this@collectToSnapShotStateList.onEach { items ->
            val sortedItems = sort(items)
            clear()
            addAll(sortedItems)
        }.launchIn(scope)
    }

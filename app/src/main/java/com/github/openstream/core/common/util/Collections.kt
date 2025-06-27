package com.github.openstream.core.common.util

fun <T> MutableList<T>.replaceFirstWith(newItem: T, predicate: (T) -> Boolean) {
    val index = indexOfFirst(predicate)
    if (index != -1) this[index] = newItem
}

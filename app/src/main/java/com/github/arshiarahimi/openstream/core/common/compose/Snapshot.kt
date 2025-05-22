package com.github.arshiarahimi.openstream.core.common.compose

import androidx.compose.runtime.snapshots.SnapshotStateList

fun <T> SnapshotStateList<T>.replaceFirstWith(newItem: T, predicate: (T) -> Boolean) {
    val index = indexOfFirst(predicate)
    if (index != -1) this[index] = newItem
}

package com.github.freetube.core.common.util

inline fun guard(closure: () -> Boolean) {
    if(closure()) return
}

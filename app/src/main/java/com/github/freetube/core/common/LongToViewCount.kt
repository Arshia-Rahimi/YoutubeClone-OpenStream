package com.github.freetube.core.common

fun Long.toViewCount(): String =
    if (this < 1000) "$this"
    else if (this < 1_000_000) (this / 100).toDouble().toString() + "K"
    else if (this < 1_000_000_000) (this / 1_000_00).toDouble().toString() + "M"
    else (this / 1_000_000_00).toDouble().toString() + "B"

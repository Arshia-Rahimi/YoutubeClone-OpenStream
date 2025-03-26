package com.github.freetube.core.common

import java.util.Locale

fun Long.toShortForm(): String =
    when {
        this < 1000L -> "$this"
        this < 1_000_000L -> divideByWithOneFloatingPoint(1000) + "K"
        this < 1_000_000_000L -> divideByWithOneFloatingPoint(1_000_000) + "M"
        else -> divideByWithOneFloatingPoint(1_000_000_000) + "B"
    }

private fun Long.divideByWithOneFloatingPoint(d: Int): String {
    require(d != 0)

    val result = this.toDouble() / d
    return if (result % 1.0 == 0.0) {
        result.toInt().toString()
    } else {
        String.format(locale = Locale("en", "US"), "%.1f", result)
    }
}

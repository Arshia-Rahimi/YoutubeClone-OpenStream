package com.github.arshiarahimi.openstream.core.common.util

import java.time.Duration
import java.time.Instant
import java.util.Locale

fun Long.toTime(): String {
    val hours = this / 3600
    val minutes = (this / 60) - (hours * 60)
    val seconds = this % 60

    val hoursString = if (hours == 0L) "" else if (hours in 1..9) "0$hours:" else "$hours:"
    val minutesString = if (minutes < 10) "0$minutes:" else "$minutes:"
    val secondsString = if (seconds < 10) "0$seconds" else "$seconds"

    return "$hoursString$minutesString$secondsString"
}

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

fun Long.timeAgo(): String {
    val past = Instant.ofEpochMilli(this)

    val now = Instant.now()
    val diff = Duration.between(past, now)

    val seconds = diff.seconds
    val minutes = diff.toMinutes()
    val hours = diff.toHours()
    val days = diff.toDays()
    val weeks = days / 7
    val months = days / 30
    val years = days / 365

    return when {
        years >= 1 -> format(years, "year")
        months >= 1 -> format(months, "month")
        weeks >= 1 -> format(weeks, "week")
        days >= 1 -> format(days, "day")
        hours >= 1 -> format(hours, "hour")
        minutes >= 1 -> format(minutes, "minute")
        seconds >= 1 -> format(seconds, "second")
        else -> "just now"
    }
}

private fun format(value: Long, unit: String) =
    if (value == 1L) "1 $unit ago" else "$value ${unit}s ago"

package com.github.openstream.core.common.util

import android.icu.util.TimeZone
import java.time.Duration
import java.time.Instant

fun getTimeZone() = TimeZone.getDefault().id

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

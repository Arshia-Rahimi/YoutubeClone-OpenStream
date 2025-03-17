package com.github.freetube.core.common.util


fun Long.toTime(): String {
    val sec = this / 1000
    val hours = sec / 3600
    val minutes = sec % 3600 / 60
    val seconds = sec % 60
    
    val hoursString = if(hours == 0L) "" else if(hours in 1..9) "0$hours:" else "$hours:"
    val minutesString = if (minutes < 10) "0$minutes:" else "$minutes:"
    val secondsString = if (seconds < 10) "0$seconds" else "$hours"
    
    return "$hoursString$minutesString$secondsString"
}

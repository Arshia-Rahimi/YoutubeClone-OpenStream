package com.github.openstream.core.common.util


fun Long.toTime(): String {
    val hours = this / 3600
    val minutes = (this / 60) - (hours * 60)
    val seconds = this % 60
    
    val hoursString = if(hours == 0L) "" else if(hours in 1..9) "0$hours:" else "$hours:"
    val minutesString = if (minutes < 10) "0$minutes:" else "$minutes:"
    val secondsString = if (seconds < 10) "0$seconds" else "$seconds"
    
    return "$hoursString$minutesString$secondsString"
}

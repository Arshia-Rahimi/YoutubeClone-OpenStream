package com.github.openstream.core.log

import android.util.Log

class DebugLogger : Logger {
    
    override fun log(content: String) {
        Log.d(getCallerInfo(), content)
    }
    
    private fun getCallerInfo(): String {
        val stackTrace = Thread.currentThread().stackTrace
        for (element in stackTrace) {
            if (!element.className.contains(Logger::class.java.name)) {
                return "${element.className}.${element.methodName}():${element.lineNumber}"
            }
        }
        return "Unknown"
    }
    
}

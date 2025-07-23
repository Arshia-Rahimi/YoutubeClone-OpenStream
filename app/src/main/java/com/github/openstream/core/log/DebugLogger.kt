package com.github.openstream.core.log

import android.util.Log

class DebugLogger : Logger {
    
    override fun i(tag: String, message: String) {
        Log.i(tag, message)
    }
    
    override fun e(tag: String, message: String, throwable: Throwable?) {
        Log.e(tag, message, throwable)
    }
    
}

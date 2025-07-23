package com.github.openstream.core.log

import android.content.Context
import android.util.Log
import com.github.openstream.core.shared.LOG_FILENAME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File

class OpenStreamLogger(
    private val scope: CoroutineScope,
    context: Context,
) : Logger {
    private val logFile: File = File(context.filesDir, LOG_FILENAME)
    
    init {
        scope.launch { logFile.createNewFile() }
    }
    
    override fun i(tag: String, message: String) {
        scope.launch {
            logFile.appendText("tag: $tag, message: $message\n")
        }
        Log.i(tag, message)
    }
    
    override fun e(tag: String, message: String, throwable: Throwable?) {
        scope.launch {
            logFile.appendText("tag: $tag, message: $message, throwable: { trace: ${throwable?.stackTrace}, cause: ${throwable?.cause}, errorMessage: ${throwable?.localizedMessage} }\n")
        }
        Log.e(tag, message, throwable)
    }
    
}

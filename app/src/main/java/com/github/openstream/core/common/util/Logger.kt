package com.github.openstream.core.common.util

import android.content.Context
import android.os.Build
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get
import java.io.File

interface Logger {
    
    fun i(tag: String, message: String)
    
    fun e(tag: String, message: String, throwable: Throwable? = null)
    
    val logStream: Flow<String>
    
}

class LoggerImp(
    private val logFileName: String,
    private val scope: CoroutineScope,
    context: Context,
) : Logger {
    
    private val logFile: File = File(context.filesDir, logFileName)
    
    override val logStream = flow {
        logFile.useLines { lines ->
            lines.forEach { emit(it) }
        }
    }
    
    init {
        scope.launch {
            if (!logFile.createNewFile()) return@launch
            logFile.appendText("log created:\n")
            val deviceInfo = mapOf(
                "manufacturer" to Build.MANUFACTURER,
                "brand" to Build.BRAND,
                "model" to Build.MODEL,
                "device" to Build.DEVICE,
                "product" to Build.PRODUCT,
                "hardware" to Build.HARDWARE,
                "board" to Build.BOARD,
                "bootloader" to Build.BOOTLOADER,
                "fingerprint" to Build.FINGERPRINT,
                "version_sdk" to Build.VERSION.SDK_INT.toString(),
                "version_release" to Build.VERSION.RELEASE,
                "version_codename" to Build.VERSION.CODENAME,
            )
            logFile.appendText("device info:\n")
            logFile.appendText("$deviceInfo\n")
        }
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

fun getLogger() = get<Logger>(Logger::class.java)

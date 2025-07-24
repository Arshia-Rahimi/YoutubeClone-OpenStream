package com.github.openstream.core.common.util

import android.content.Context
import android.os.Build
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

interface Logger {
    
    fun i(tag: String?, message: String?)
    
    fun e(tag: String?, message: String?, throwable: Throwable? = null)
    
    val logStream: Flow<String>
    
}

class LoggerImpl(
    private val scope: CoroutineScope,
    logFileName: String,
    context: Context,
) : Logger {
    
    private val logFile: File = File(context.filesDir, logFileName)
    
    override val logStream = flow {
        while (!logFile.exists()) delay(1000)
        
        // emit existing lines
        logFile.readLines().joinToString("\n").let { emit(it) }
        
        BufferedReader(FileReader(logFile)).use { reader ->
            // Skip existing lines
            repeat(logFile.readLines().size) {
                reader.readLine()
            }
            
            while (currentCoroutineContext().isActive) {
                reader.readLine()?.let { emit(it) }
                delay(1000)
            }
        }
    }.flowOn(Dispatchers.IO)
    
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
    
    override fun i(tag: String?, message: String?) {
        scope.launch {
            logFile.appendText("tag: $tag, message: $message\n")
        }
        Log.i("log-$tag", message ?: "")
    }
    
    override fun e(tag: String?, message: String?, throwable: Throwable?) {
        scope.launch {
            logFile.appendText("tag: $tag, message: $message, throwable: { trace: ${throwable?.stackTrace}, cause: ${throwable?.cause}, errorMessage: ${throwable?.localizedMessage} }\n")
        }
        Log.e("log-$tag", message, throwable)
    }
    
}

fun getLogger() = get<Logger>(Logger::class.java)

package com.github.openstream.core.common.util

import kotlinx.coroutines.flow.Flow
import org.koin.java.KoinJavaComponent.get

interface Logger {
    
    fun i(tag: String, message: String)
    
    fun e(tag: String, message: String, throwable: Throwable? = null)
    
    val logStream: Flow<String>
    
}

fun getLogger() = get<Logger>(Logger::class.java)

package com.github.openstream.core.log

import org.koin.java.KoinJavaComponent.get

interface Logger {
    
    fun i(tag: String, message: String)
    
    fun e(tag: String, message: String, throwable: Throwable? = null)
}

fun getLogger() = get<Logger>(Logger::class.java)

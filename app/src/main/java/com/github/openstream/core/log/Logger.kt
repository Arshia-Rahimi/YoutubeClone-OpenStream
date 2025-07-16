package com.github.openstream.core.log

import org.koin.java.KoinJavaComponent.get

interface Logger {
    fun log(content: String)
}

fun getLogger() = get<Logger>(Logger::class.java)

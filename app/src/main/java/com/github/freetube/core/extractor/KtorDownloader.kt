package com.github.freetube.core.extractor

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.headers
import io.ktor.util.toMap
import io.ktor.utils.io.InternalAPI
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okio.IOException
import org.schabi.newpipe.extractor.downloader.Downloader
import org.schabi.newpipe.extractor.downloader.Request
import org.schabi.newpipe.extractor.downloader.Response
import org.schabi.newpipe.extractor.exceptions.ExtractionException

class KtorDownloader(
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
): Downloader() {
    
    private val client = HttpClient(CIO) {
        engine { 
            requestTimeout = 30_000
        }
        install(DefaultRequest) {
            header(HttpHeaders.UserAgent, "Mozilla/5.0 (Windows NT 10.0; rv:91.0) Gecko/20100101 Firefox/91.0")
        }
    }
    
    @OptIn(InternalAPI::class)
    override fun execute(request: Request): Response? =
        runBlocking(coroutineDispatcher) {
            try {
                val response = client.request(request.url()) {
                    method = HttpMethod.parse(request.httpMethod())
                    headers {
                        request.headers().forEach { k, v ->
                            append(k, v.joinToString(", "))
                        }
                    }
                    request.dataToSend()?.let {
                        body = it
                    }
                }

                Response(
                    response.status.value,
                    "",
                    response.headers.toMap(),
                    response.body(),
                    response.call.request.url.toString(),
                )
            } catch (e: Exception) {
                when (e) {
                    is IOException -> throw e
                    else -> throw ExtractionException("Ktor download failed", e)
                }
            }
        }
}

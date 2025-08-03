package com.github.openstream.core.extractor

import com.github.openstream.core.data.PreferencesRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.UserAgent
import io.ktor.client.request.cookie
import io.ktor.client.request.headers
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.HttpMethod
import io.ktor.util.toMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import org.schabi.newpipe.extractor.downloader.Downloader
import org.schabi.newpipe.extractor.downloader.Request
import org.schabi.newpipe.extractor.downloader.Response

class KtorDownloader(
    scope: CoroutineScope,
    preferencesRepo: PreferencesRepository,
) : Downloader() {
    
    private val customCookies = preferencesRepo.preferences
        .map {
            it.cookies
                ?.lineSequence()
                ?.filter { line -> line.isNotBlank() && !line.startsWith("#") }
                ?.mapNotNull { line ->
                    val parts = line.split('\t')
                    if (parts.size >= 7) {
                        val name = parts[5]
                        val value = parts[6]
                        name to value
                    } else null
                }
                ?.toMap()
        }
        .stateIn(scope, SharingStarted.WhileSubscribed(5000), null)
    
    private val client = HttpClient(CIO) {
        install(UserAgent) {
            agent =
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:128.0) Gecko/20100101 Firefox/128.0"
        }
    }
    
    override fun execute(request: Request): Response? = runBlocking {
        return@runBlocking client.request(request.url()) {
            method = HttpMethod(request.httpMethod())
            
            headers {
                request.headers().forEach { k, v ->
                    remove(k)
                    v.forEach {
                        append(k, it)
                    }
                }
            }
            
            request.dataToSend()?.let {
                setBody(it)
            }
            
            customCookies.value?.forEach {
                cookie(it.key, it.value)
            }
        }.toExtractorResponse()
    }
    
    suspend fun HttpResponse.toExtractorResponse() = Response(
        status.value,
        status.description,
        headers.toMap(),
        bodyAsText(),
        request.url.toString(),
    )
    
}

package com.github.openstream.core.extractor

import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import org.schabi.newpipe.extractor.downloader.Downloader
import org.schabi.newpipe.extractor.downloader.Request
import org.schabi.newpipe.extractor.downloader.Response
import org.schabi.newpipe.extractor.exceptions.ReCaptchaException
import java.util.concurrent.TimeUnit

object NewPipeDownloader : Downloader() {
    
    const val USER_AGENT =
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:128.0) Gecko/20100101 Firefox/128.0"
    
    private val mCookies = emptyMap<String, String>()
    private val client = OkHttpClient.Builder()
        .readTimeout(30, TimeUnit.SECONDS)
        .build()
    
    override fun execute(request: Request): Response? {
        val method = request.httpMethod()
        val url = request.url()
        val headers = request.headers()
        val requestBody = request.dataToSend()?.toRequestBody()
        
        val cookies = mCookies.map {
            it.key
        }
        val requestBuilder = okhttp3.Request.Builder()
            .method(method, requestBody)
            .url(url)
            .addHeader("User-Agent", USER_AGENT)
        
        headers.forEach {
            requestBuilder.removeHeader(it.key)
            it.value.forEach { header ->
                requestBuilder.addHeader(it.key, header)
            }
        }
        
        val response = client.newCall(requestBuilder.build()).execute()
        
        if (response.code == 429) {
            throw ReCaptchaException("reCaptcha Challenge requested", url)
        }
        
        val responseBodyToReturn = response.body.toString()
        val latestUrl = response.request.url.toString()
        
        return Response(
            response.code,
            response.message,
            response.headers.toMultimap(),
            responseBodyToReturn,
            latestUrl,
        )
    }
    
}
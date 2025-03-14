package com.github.freetube.core.extractor

import okhttp3.Headers.Companion.toHeaders
import okhttp3.OkHttpClient
import org.schabi.newpipe.extractor.downloader.Downloader
import org.schabi.newpipe.extractor.downloader.Request
import org.schabi.newpipe.extractor.downloader.Response

class OkHttpDownloader: Downloader() {

    private val client = OkHttpClient()

    override fun execute(request: Request): Response? {
        val okHttpsRequest = okhttp3.Request.Builder()
            .url(request.url())
            .headers(
                request.headers().mapValues<String, List<String>, String> { 
                    it.value.joinToString(", ")
                }.toHeaders()
            )
            .build()
        
        client.newCall(okHttpsRequest).execute().use { res ->
            return Response(
                res.code,
                res.message,
                null,
                res.body.toString(),
                null
            )
        }
    }
}

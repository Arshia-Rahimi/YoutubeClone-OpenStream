package com.github.openstream.ui.feature.settings.webview

import android.annotation.SuppressLint
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.viewinterop.AndroidView
import com.github.openstream.core.data.PreferencesRepository
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun YoutubeLoginWebView() {
    val loginUrl = ""
    val targetDomain = ".youtube.com"
    val preferencesRepo: PreferencesRepository = koinInject()
    val scope = rememberCoroutineScope()
    
    AndroidView(
        factory = { context ->
            WebView(context).apply webview@ {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true

                CookieManager.getInstance().apply {
                    setAcceptCookie(true)
                    setAcceptThirdPartyCookies(this@webview, true)
                }

                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        val cookies = CookieManager.getInstance().getCookie(targetDomain)
                        if (!cookies.isNullOrEmpty()) {
                            scope.launch {
                                preferencesRepo.setCookies(cookies)
                            }
                        }
                    }
                }

                loadUrl(loginUrl)
            }
        }
    )
}

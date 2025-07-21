package com.github.openstream.ui.global.player.components

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.core.text.isDigitsOnly
import com.github.openstream.ui.global.player.PlayerAction
import java.net.URI

private fun String.getPath() = URI(this).path

private fun String.getUrlParam(paramName: String): String? {
    val uri = URI(this)
    val query = uri.query ?: return null
    
    return query.split("&")
        .mapNotNull {
            val parts = it.split("=", limit = 2)
            if (parts.size == 2) parts[0] to parts[1] else null
        }.toMap()[paramName]
}

private fun String.getBaseUrl(): String {
    val uri = URI(this)
    val portPart = if (uri.port != -1 && uri.port != uri.defaultPort()) ":${uri.port}" else ""
    return "${uri.scheme}://${uri.host}$portPart"
}

private fun URI.defaultPort(): Int = when (scheme) {
    "http" -> 80
    "https" -> 443
    else -> -1
}

fun onLinkClicked(currentVideoUrl: String, context: Context, link: String) {
    if (link.getBaseUrl() != "https://youtube.com") {
        val intent = Intent(Intent.ACTION_VIEW, link.toUri())
        context.startActivity(intent)
    }
    
    val path = link.getPath()
    when (path) {
        "/watch" -> {
            if (link.getUrlParam("v") == currentVideoUrl.getUrlParam("v")) {
                val timeString = link.getUrlParam("t")
                val time =
                    if (timeString?.isDigitsOnly() ?: false) timeString.toLong() * 1000 else return
                PlayerAction.SeekTo(time).send()
            }
        }
        // todo: add all paths
        else -> return
    }
}

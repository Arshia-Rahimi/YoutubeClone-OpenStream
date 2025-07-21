package com.github.openstream.ui.global.player.components

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

fun onLinkClicked(currentVideoUrl: String, link: String) {
    // todo: add external deep link if authority isn't youtube.com
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

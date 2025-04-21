package com.github.openstream.core.shared.exceptions

class OfflineFirstPlaylistNotFetchedException: Exception() {
    override val message = "fetch the playlist first to sync it with youtube"
}
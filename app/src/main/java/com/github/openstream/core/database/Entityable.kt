package com.github.openstream.core.database

interface Entityable {
    fun toEntity(channelId: Int?, playlistId: Int?): Any
}

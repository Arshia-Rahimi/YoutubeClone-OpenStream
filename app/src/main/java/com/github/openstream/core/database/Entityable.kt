package com.github.openstream.core.database

interface OpenStreamEntity

interface Entityable {
    fun toEntity(channelId: Int?, playlistId: Int?): OpenStreamEntity
}

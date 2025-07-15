package com.github.openstream.core.shared.dataitem

interface OpenStreamEntity

interface Entityable {
    fun toEntity(): OpenStreamEntity
}

package com.github.openstream.core.model.extractor

interface OpenStreamEntity

interface Entityable {
    fun toEntity(): OpenStreamEntity
}

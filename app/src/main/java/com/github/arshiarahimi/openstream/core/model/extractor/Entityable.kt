package com.github.arshiarahimi.openstream.core.model.extractor

interface OpenStreamEntity

interface Entityable {
    fun toEntity(): OpenStreamEntity
}

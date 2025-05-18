package com.github.arshiarahimi.openstream.core.database

interface OpenStreamEntity

interface Entityable {
    fun toEntity(): OpenStreamEntity
}

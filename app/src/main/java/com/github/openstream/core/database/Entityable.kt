package com.github.openstream.core.database

interface Entityable {
    fun toEntity(parentId: Int?): Any
}

package com.github.freetube.core.database

interface Entityable {
    fun toEntity(parentId: Int?): Any
}

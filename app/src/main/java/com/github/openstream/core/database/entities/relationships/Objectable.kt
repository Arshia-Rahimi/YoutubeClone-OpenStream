package com.github.openstream.core.database.entities.relationships

import com.github.openstream.core.model.extractordata.ViewableObject

// todo change name
interface Objectable {
    fun toObject(): ViewableObject
}

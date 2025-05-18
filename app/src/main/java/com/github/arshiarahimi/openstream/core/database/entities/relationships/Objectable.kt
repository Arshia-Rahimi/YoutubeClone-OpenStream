package com.github.arshiarahimi.openstream.core.database.entities.relationships

import com.github.arshiarahimi.openstream.core.model.extractordata.ViewableObject

// todo change name
interface Objectable {
    fun toObject(): ViewableObject
}

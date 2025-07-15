package com.github.openstream.core.database

import androidx.room.TypeConverter
import com.github.openstream.core.shared.extractor.data.ChannelTab
import kotlinx.serialization.json.Json

class OpenStreamRoomConverter {

    @TypeConverter
    fun fromListOfChannelTabs(tabs: List<ChannelTab>?) = tabs?.let { Json.encodeToString(it) }

    @TypeConverter
    fun toListOfChannelTabs(json: String?) =
        json?.let { Json.decodeFromString<List<ChannelTab>>(it) }

}

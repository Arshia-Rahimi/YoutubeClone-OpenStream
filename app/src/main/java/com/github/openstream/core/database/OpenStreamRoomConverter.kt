package com.github.openstream.core.database

import androidx.room.TypeConverter
import com.github.openstream.core.model.extractordata.ChannelTab
import kotlinx.serialization.json.Json

class OpenStreamRoomConverter {

    @TypeConverter
    fun fromListOfChannelTabsToString(tabs: List<ChannelTab>) =
        Json.encodeToString(tabs)

    fun toListOfChannelTabs(json: String) =
        Json.decodeFromString<List<ChannelTab>>(json)
}

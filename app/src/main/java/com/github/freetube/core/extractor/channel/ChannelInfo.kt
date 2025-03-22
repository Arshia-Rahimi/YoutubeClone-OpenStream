package com.github.freetube.core.extractor.channel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.github.freetube.core.extractor.model.DataItem

data class ChannelInfo(
    val name: String,
    val subscriberCount: Long,
    val description: String,
    val avatar: String,
    val verified: Boolean,
    val banner: String,
    val tabs: List<ChannelTab>,
    val id: String,
)

data class ChannelTab(
    val name: String,
    val url: String,
    val items: SnapshotStateList<DataItem> = mutableStateListOf(),
    val isLoading: Boolean = true,
    val error: String? = null,
)

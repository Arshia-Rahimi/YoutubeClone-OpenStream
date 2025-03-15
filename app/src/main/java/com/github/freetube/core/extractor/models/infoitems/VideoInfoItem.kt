package com.github.freetube.core.extractor.models.infoitems

data class VideoInfoItem(
    override val url: String,
    override val name: String,
): InfoItem

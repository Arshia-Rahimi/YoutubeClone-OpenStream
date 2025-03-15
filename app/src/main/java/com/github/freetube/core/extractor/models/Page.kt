package com.github.freetube.core.extractor.models

import com.github.freetube.core.extractor.Item
import org.schabi.newpipe.extractor.Page

data class Page(
    val items: List<Item>,
    val hasNextPage: Boolean,
    val nextPage: Page? = null,
)

package com.github.openstream.ui.feature.library.components

import androidx.annotation.StringRes
import com.github.openstream.R

enum class SortType(
    @StringRes val string: Int,
) {
    CREATED_AT_ASC(
        string = R.string.sort_by_created_at_asc,
    ),
    CREATED_AT_DESC(
        string = R.string.sort_by_created_at_desc,
    ),
    NAME_ASC(
        string = R.string.sort_by_name_asc,
    ),
    NAME_DESC(
        string = R.string.sort_by_name_desc,
    ),
    SIZE_ASC(
        string = R.string.sort_by_size_asc,
    ),
    SIZE_DESC(
        string = R.string.sort_by_size_desc,
    ),
}

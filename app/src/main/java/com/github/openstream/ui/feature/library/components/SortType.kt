package com.github.openstream.ui.feature.library.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.arshia.openstream.R

enum class SortType(
    @StringRes val string: Int,
    @DrawableRes val icon: Int,
) {
    CREATED_AT_ASC(
        string = R.string.sort_by_created_at_asc,
        icon = R.drawable.up_arrow,
    ),
    CREATED_AT_DESC(
        string = R.string.sort_by_created_at_desc,
        icon = R.drawable.down_arrow,
    ),
    NAME_ASC(
        string = R.string.sort_by_name_asc,
        icon = R.drawable.up_arrow,
    ),
    NAME_DESC(
        string = R.string.sort_by_name_desc,
        icon = R.drawable.down_arrow,
    ),
    SIZE_ASC(
        string = R.string.sort_by_size_asc,
        icon = R.drawable.up_arrow,
    ),
    SIZE_DESC(
        string = R.string.sort_by_size_desc,
        icon = R.drawable.down_arrow,
    ),
}

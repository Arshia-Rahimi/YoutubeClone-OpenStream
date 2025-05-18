package com.github.arshiarahimi.openstream.core.model.enums

import androidx.annotation.StringRes
import com.github.arshiarahimi.openstream.R

enum class SubscriptionsSortType(
    @StringRes val string: Int,
) {
    DATE_ADDED_ASC(
        string = R.string.sort_by_date_added_asc,
    ),
    DATE_ADDED_DESC(
        string = R.string.sort_by_date_added_desc,
    ),
    MOST_SUBSCRIBERS(
        string = R.string.sort_by_most_subs
    ),
    FEWEST_SUBSCRIBERS(
        string = R.string.sort_by_fewest_subs
    )
}

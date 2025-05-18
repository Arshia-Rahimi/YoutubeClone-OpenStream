package com.github.arshiarahimi.openstream.ui.global.popups.confirmationdialog

import androidx.annotation.StringRes
import com.github.arshiarahimi.openstream.R
import com.github.arshiarahimi.openstream.core.model.extractordata.ChannelItem
import com.github.arshiarahimi.openstream.core.model.extractordata.OfflineFirstChannel
import com.github.arshiarahimi.openstream.core.model.extractordata.PlaylistItem

sealed interface Confirmation {
    val confirmButton: Int
    val confirmationText: Int
}

data class UnsubscribeItem(
    val channel: ChannelItem.OfflineFirstChannelItem,
) : Confirmation {
    @StringRes
    override val confirmButton = R.string.confirm_unsubscribe

    @StringRes
    override val confirmationText = R.string.unsubscribe
}

data class Unsubscribe(
    val channel: OfflineFirstChannel,
) : Confirmation {
    @StringRes
    override val confirmButton = R.string.confirm_unsubscribe

    @StringRes
    override val confirmationText = R.string.unsubscribe
}

data class DeletePlaylistItem(
    val playlist: PlaylistItem.LocalPlaylistItem,
) : Confirmation {
    @StringRes
    override val confirmButton = R.string.delete

    @StringRes
    override val confirmationText = R.string.confirm_delete_playlist
}

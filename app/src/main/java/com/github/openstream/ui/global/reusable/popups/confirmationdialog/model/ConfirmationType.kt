package com.github.openstream.ui.global.reusable.popups.confirmationdialog.model

import androidx.annotation.StringRes
import com.github.openstream.R
import com.github.openstream.core.shared.dataitem.PlaylistItem

sealed interface Confirmation {
    val confirmButton: Int
    val confirmationText: Int
    
    data class UnsubscribeItem(
        val channelId: Long,
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
    
    data class SavePlaylistItem(
        val playlist: PlaylistItem.OnlinePlaylistItem,
    ) : Confirmation {
        @StringRes
        override val confirmButton = R.string.save_playlist
        
        @StringRes
        override val confirmationText = R.string.confirm_save_playlist
    }
}


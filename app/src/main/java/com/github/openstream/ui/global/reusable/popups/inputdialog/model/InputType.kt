package com.github.openstream.ui.global.reusable.popups.inputdialog.model

import com.github.openstream.R

sealed interface InputType {
    val confirmButton: Int
    
    data class CreatePlaylist(
        override val confirmButton: Int = R.string.create,
    ) : InputType
    
    data class SearchPlaylist(
        override val confirmButton: Int = R.string.find_playlist,
    ) : InputType
}

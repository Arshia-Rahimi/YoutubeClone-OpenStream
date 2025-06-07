package com.github.arshiarahimi.openstream.ui.global.popups.inputdialog

import com.github.arshiarahimi.openstream.R

sealed interface InputType {
    val confirmButton: Int
}

data class CreatePlaylist(
    override val confirmButton: Int = R.string.create,
) : InputType

data class SearchPlaylist(
    override val confirmButton: Int = R.string.find_playlist,
) : InputType

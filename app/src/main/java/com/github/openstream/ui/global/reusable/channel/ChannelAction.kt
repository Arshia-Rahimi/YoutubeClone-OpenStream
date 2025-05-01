package com.github.openstream.ui.global.reusable.channel

sealed class ChannelAction(val tab: Int) {
    data class GetTab(val index: Int) : ChannelAction(index)
    data class GetTabNextPage(val index: Int) : ChannelAction(index)
}

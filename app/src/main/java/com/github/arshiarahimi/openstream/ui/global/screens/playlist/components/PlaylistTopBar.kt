package com.github.arshiarahimi.openstream.ui.global.screens.playlist.components

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.arshiarahimi.openstream.R
import com.github.arshiarahimi.openstream.core.model.dataitem.PlaylistItem
import com.github.arshiarahimi.openstream.core.shared.LIKED_VIDEOS_ID
import com.github.arshiarahimi.openstream.core.shared.WATCH_LATER_ID
import com.github.arshiarahimi.openstream.ui.global.popups.PopupController
import com.github.arshiarahimi.openstream.ui.global.popups.confirmationdialog.DeletePlaylistItem
import com.github.arshiarahimi.openstream.ui.global.popups.confirmationdialog.SavePlaylistItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistTopBar(
    playlist: PlaylistItem,
    toChannelScreen: (String) -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                Column {
                    Text(
                        text = playlist.name,
                        fontSize = 20.sp,
                        maxLines = 1,
                        modifier = Modifier.basicMarquee(),
                    )
                    if (playlist is PlaylistItem.YoutubePlaylistItem) {
                        Text(
                            text = playlist.channelName,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onTertiary,
                            modifier = Modifier.clickable { toChannelScreen(playlist.channelUrl) },
                        )
                    }
                }
                Spacer(Modifier.weight(1f))
                Text(
                    text = playlist.count.toString() + " videos",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onTertiary,
                )
            }
        },
        actions = {
            when (playlist) {
                is PlaylistItem.OnlinePlaylistItem -> {
                    IconButton(
                        onClick = { PopupController.openConfirmationDialog(SavePlaylistItem(playlist)) }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.plus),
                            contentDescription = "save playlist"
                        )
                    }
                }
                
                is PlaylistItem.LocalPlaylistItem -> {
                    if (playlist.id != WATCH_LATER_ID && playlist.id != LIKED_VIDEOS_ID) {
                        IconButton(
                            onClick = {
                                PopupController.openConfirmationDialog(
                                    DeletePlaylistItem(
                                        playlist
                                    )
                                )
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.cross),
                                contentDescription = "delete playlist"
                            )
                        }
                    }
                }
            }
        }
    )
}

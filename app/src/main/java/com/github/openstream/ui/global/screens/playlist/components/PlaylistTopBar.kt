package com.github.openstream.ui.global.screens.playlist.components

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
import com.github.openstream.R
import com.github.openstream.core.shared.DefaultPlaylists
import com.github.openstream.core.shared.dataitem.PlaylistItem
import com.github.openstream.ui.global.popups.PopupController
import com.github.openstream.ui.global.popups.confirmationdialog.model.DeletePlaylistItem
import com.github.openstream.ui.global.popups.confirmationdialog.model.SavePlaylistItem

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
                    if (playlist.id !in DefaultPlaylists.all) {
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

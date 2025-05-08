package com.github.openstream.ui.designsystem.dataitem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.github.openstream.R
import com.github.openstream.core.model.extractordata.DataItem

@Composable
fun Playlist(
    modifier: Modifier,
    item: DataItem.Playlist,
    toChannelScreen: (String) -> Unit,
    toPlaylistScreen: (DataItem.Playlist) -> Unit,
    shouldViewChannel: Boolean,
    deletePlaylist: (DataItem.Playlist) -> Unit,
    savePlaylist: (DataItem.Playlist) -> Unit,
) {
    var isDropDownExpanded by remember { mutableStateOf(false) }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable { toPlaylistScreen(item) }
            .clip(RoundedCornerShape(12.dp)),
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.4f)
                .padding(end = 8.dp)
                .clip(RoundedCornerShape(16.dp)),
        ) {
            AsyncImage(
                model = item.thumbnail,
                contentDescription = "thumbnail",
                modifier = Modifier.matchParentSize(),
            )
            Row(
                modifier = Modifier
                    .padding(bottom = 4.dp, end = 4.dp)
                    .align(Alignment.BottomEnd)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black.copy(0.5f))
                    .padding(vertical = 4.dp, horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Icon(
                    painter = painterResource(R.drawable.playlist),
                    contentDescription = null,
                    tint = Color.Unspecified,
                )
                Text(
                    text = item.count.toString(),
                    fontSize = 12.sp,
                    color = Color.White,
                )
            }
        }
        Column(
            modifier = Modifier.weight(0.6f),
            verticalArrangement = Arrangement.Top,
        ) {
            Text(
                text = item.name,
                fontSize = 20.sp,
                lineHeight = 24.sp,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .fillMaxWidth()
                    .weight(1f)
            )
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                when (item) {
                    is DataItem.Playlist.LocalPlaylist -> Unit
                    is DataItem.Playlist.OnlinePlaylist -> {
                        SubText(text = item.channelName)
                        if (item.isChannelVerified == true) {
                            Icon(
                                modifier = Modifier.padding(start = 4.dp, end = 8.dp),
                                painter = painterResource(R.drawable.verified),
                                contentDescription = "verified",
                                tint = Color(0xFFAAAAAA)
                            )
                        }
                    }
                    
                    is DataItem.Playlist.OfflineFirstPlaylist -> {
                        SubText(text = item.channelName)
                        if (item.isChannelVerified == true) {
                            Icon(
                                modifier = Modifier.padding(start = 4.dp, end = 8.dp),
                                painter = painterResource(R.drawable.verified),
                                contentDescription = "verified",
                                tint = Color(0xFFAAAAAA)
                            )
                        }
                    }
                }
            }
            SubText("")
        }
        Box(
            modifier = Modifier
                .align(Alignment.Top)
                .width(24.dp)
        ) {
            IconButton(
                modifier = Modifier,
                onClick = { isDropDownExpanded = !isDropDownExpanded },
            ) {
                Icon(
                    painter = painterResource(R.drawable.options),
                    contentDescription = "options",
                    tint = Color.White,
                )
            }
            DropdownMenu(
                expanded = isDropDownExpanded,
                onDismissRequest = { isDropDownExpanded = false },
                tonalElevation = 4.dp,
            ) {
                when (item) {
                    is DataItem.Playlist.OnlinePlaylist -> {
                        if (shouldViewChannel) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.view_channel)) },
                                onClick = {
                                    isDropDownExpanded = false
                                    toChannelScreen(item.channelUrl)
                                },
                            )
                        }
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.save_playlist)) },
                            onClick = {
                                isDropDownExpanded = false
                                savePlaylist(item)
                            }
                        )
                    }
                    
                    is DataItem.Playlist.OfflineFirstPlaylist -> {
                        if (shouldViewChannel) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.view_channel)) },
                                onClick = {
                                    isDropDownExpanded = false
                                    toChannelScreen(item.channelUrl)
                                },
                            )
                        }
                        // can't delete watch later
                        if (item.id != 0L) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.delete_playlist)) },
                                onClick = {
                                    isDropDownExpanded = false
                                    deletePlaylist(item)
                                }
                            )
                        }
                    }
                    
                    is DataItem.Playlist.LocalPlaylist -> {
                        // can't delete watch later
                        if (item.id != 0L) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.delete_playlist)) },
                                onClick = {
                                    isDropDownExpanded = false
                                    deletePlaylist(item)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MaterialTheme {
        Playlist(
            item = DataItem.Playlist.OnlinePlaylist(
                name = "name",
                channelUrl = "",
                channelName = "channel",
                isChannelVerified = true,
                url = "",
                thumbnail = "",
                count = 25L,
            ),
            toChannelScreen = {},
            toPlaylistScreen = {},
            shouldViewChannel = true,
            modifier = Modifier,
            savePlaylist = {},
            deletePlaylist = {},
        )
    }
}

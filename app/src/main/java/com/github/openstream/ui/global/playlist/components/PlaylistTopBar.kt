package com.github.openstream.ui.global.playlist.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.openstream.core.extractor.playlist.PlaylistMetadata

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistTopBar(
    playlist: PlaylistMetadata,
    toChannelScreen: (String) -> Unit,
//    scrollBehavior: TopAppBarScrollBehavior,
) {
    CenterAlignedTopAppBar(
//        scrollBehavior = scrollBehavior,
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
                    )
                    Text(
                        text = playlist.channelName,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onTertiary,
                        modifier = Modifier.clickable { toChannelScreen(playlist.channelUrl) },
                    )
                }
                Spacer(Modifier.weight(1f))
                Text(
                    text = playlist.count.toString() + " videos",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onTertiary,
                )
            }
        }
    )
}

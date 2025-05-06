package com.github.openstream.ui.feature.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arshia.openstream.R
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.ui.designsystem.dataitem.DataItemList
import com.github.openstream.ui.global.components.createplaylistdialog.CreatePlaylistDialog
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    topBar: (@Composable () -> Unit) -> Unit,
    toChannelScreen: (String) -> Unit,
    toPlaylistScreen: (DataItem.Playlist) -> Unit,
    playVideo: (String) -> Unit,
) {
    val viewModel = koinViewModel<LibraryViewModel>()
    val playlists = viewModel.playlists
    var newPlaylistTitle by remember { mutableStateOf("") }
    var showCreatePlaylistDialog by remember { mutableStateOf(false) }
    val sortType by viewModel.sortType.collectAsStateWithLifecycle()

    topBar {
        TopAppBar(
            title = {
                Text(stringResource(R.string.library))
            },
            actions = {
                IconButton(
                    onClick = { showCreatePlaylistDialog = true },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.plus),
                        contentDescription = stringResource(R.string.cd_plus_icon)
                    )
                }
            }
        )
    }

    if (showCreatePlaylistDialog) {
        CreatePlaylistDialog(
            dismiss = {
                showCreatePlaylistDialog = false
                newPlaylistTitle = ""
            },
            newPlaylistTitle = newPlaylistTitle,
            setNewPlaylistName = { newPlaylistTitle = it },
            createPlaylist = { viewModel.createPlaylist(newPlaylistTitle) },
        )
    }
    
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.End,
    ) {
        Row(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .padding(end = 16.dp)
                .clickable { viewModel.toggleSortType() },
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = stringResource(sortType.string),
                fontSize = 16.sp,
                color = Color.White,
            )
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = stringResource(sortType.string),
                tint = Color.White,
            )
        }
        DataItemList(
            items = playlists,
            toChannelScreen = toChannelScreen,
            toPlaylistScreen = toPlaylistScreen,
            playVideo = playVideo,
            savePlaylist = { viewModel.savePlaylist(it) },
            deletePlaylist = { viewModel.deletePlaylist(it) },
        )
    }
}


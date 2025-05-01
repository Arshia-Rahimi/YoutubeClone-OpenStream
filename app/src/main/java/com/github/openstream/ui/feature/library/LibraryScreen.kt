package com.github.openstream.ui.feature.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arshia.openstream.R
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.ui.designsystem.components.DataItemList
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
            onDismiss = {
                showCreatePlaylistDialog = false
                newPlaylistTitle = ""
            },
            newPlaylistTitle = newPlaylistTitle,
            setNewPlaylistName = { newPlaylistTitle = it },
            createPlaylist = { viewModel.createPlaylist(newPlaylistTitle) },
        )
    }

    DataItemList(
        items = playlists,
        toChannelScreen = toChannelScreen,
        toPlaylistScreen = toPlaylistScreen,
        playVideo = playVideo,
    )
}

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreatePlaylistDialog(
    onDismiss: () -> Unit = {},
    newPlaylistTitle: String = "",
    setNewPlaylistName: (String) -> Unit = {},
    createPlaylist: () -> Unit = {},
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
    ) {
        Column(
            modifier = Modifier.padding(4.dp),
            verticalArrangement = Arrangement.spacedBy(
                4.dp,
                alignment = Alignment.CenterVertically,
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TextField(
                value = newPlaylistTitle,
                onValueChange = setNewPlaylistName,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                Button(
                    onClick = onDismiss,
                ) {
                    Text(stringResource(R.string.dismiss))
                }
                Button(
                    onClick = {
                        createPlaylist()
                        onDismiss()
                    },
                ) {
                    Text(stringResource(R.string.create))
                }
            }
        }
    }
}

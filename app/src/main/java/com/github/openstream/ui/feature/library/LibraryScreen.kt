package com.github.openstream.ui.feature.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arshia.openstream.R
import com.github.openstream.core.common.compose.ObserveForEvents
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.ui.designsystem.components.DataItemList
import org.koin.androidx.compose.koinViewModel
import org.schabi.newpipe.extractor.timeago.patterns.vi

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
    var playlistCreationError by remember { mutableStateOf<Pair<String, String>?>(null) }
    
    ObserveForEvents(viewModel.playlistActionErrorEvent) {
        playlistCreationError = it
    }

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
    
    playlistCreationError?.let {
        PlaylistActionErrorDialog(
            dismiss = { playlistCreationError = null },
            error = it,
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

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreatePlaylistDialog(
    dismiss: () -> Unit = {},
    newPlaylistTitle: String = "",
    setNewPlaylistName: (String) -> Unit = {},
    createPlaylist: () -> Unit = {},
) {
    Dialog(
        dismiss = dismiss,
    ) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(
                4.dp,
                alignment = Alignment.CenterVertically,
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TextField(
                modifier = Modifier.clip(RoundedCornerShape(4.dp)),
                value = newPlaylistTitle,
                onValueChange = setNewPlaylistName,
                singleLine = true,
                keyboardActions = KeyboardActions(
                    onDone = {
                        createPlaylist()
                        dismiss()
                    }
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                ),
                colors = TextFieldDefaults.colors().copy(
                    focusedContainerColor = Color(0xFF1D1D1D),
                    unfocusedContainerColor = Color(0xFF1D1D1D),
                    focusedIndicatorColor = Color(0xFF1D1D1D),
                    unfocusedIndicatorColor = Color(0xFF1D1D1D),
                ),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.End),
            ) {
                Button(
                    onClick = dismiss,
                ) {
                    Text(stringResource(R.string.dismiss))
                }
                Button(
                    onClick = {
                        createPlaylist()
                        dismiss()
                    },
                ) {
                    Text(stringResource(R.string.create))
                }
            }
        }
    }
}

@Composable
fun PlaylistActionErrorDialog(
    error: Pair<String, String>,
    dismiss: () -> Unit,
) {
    Dialog(
        dismiss = dismiss,
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = error.first,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                Button(
                    onClick = dismiss,
                ) {
                    Text(stringResource(R.string.dismiss))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Dialog(
    dismiss: () -> Unit,
    content: @Composable () -> Unit,
) {
    BasicAlertDialog(
        onDismissRequest = dismiss,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .padding(16.dp)
    ) {
        content()
    }
}
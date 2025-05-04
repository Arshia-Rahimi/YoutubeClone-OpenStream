package com.github.openstream.ui.global.reusable.dialogs.addtoplaylist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arshia.openstream.R
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.ui.designsystem.components.CreatePlaylistDialog
import com.github.openstream.ui.designsystem.components.OpenStreamDialog
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AddToPlaylistDialog(
    video: DataItem.Video,
    dismiss: () -> Unit,
) {
    val viewModel = koinViewModel<AddToPlaylistViewModel>(parameters = { parametersOf(video) })
    var showCreatePlaylistDialog by remember { mutableStateOf(false) }
    var newPlaylistTitle by remember { mutableStateOf("") }

    if (showCreatePlaylistDialog) {
        CreatePlaylistDialog(
            dismiss = { showCreatePlaylistDialog = false },
            newPlaylistTitle = newPlaylistTitle,
            setNewPlaylistName = { newPlaylistTitle = it },
            createPlaylist = { viewModel.createPlaylist(newPlaylistTitle) },
        )
    }

    OpenStreamDialog(
        dismiss = dismiss,
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    IconButton(
                        onClick = { showCreatePlaylistDialog = false }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.plus),
                            contentDescription = "create new playlist"
                        )
                    }
                }
            }
            items(
                items = viewModel.localPlaylists.keys.toList(),
                key = { it.key },
                contentType = { it },
            ) { playlist ->
                val isChecked = viewModel.localPlaylists[playlist] == true
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(playlist.name)
                    Checkbox(
                        onCheckedChange = {
                            viewModel.localPlaylists[playlist] = it
                        },
                        checked = isChecked
                    )
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    Button(
                        onClick = {
                            viewModel.syncVideoInPlaylists()
                            dismiss()
                        }
                    ) {
                        Text(stringResource(R.string.done), fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

package com.github.openstream.ui.global.components.addtoplaylistmodal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.openstream.R
import com.github.openstream.core.model.extractordata.DataItem
import com.github.openstream.ui.global.components.PopupController
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveVideoToPlaylistsModal(
    video: DataItem.Video,
) {
    val viewModel =
        koinViewModel<SaveVideoToPlaylistsViewModel>(parameters = { parametersOf(video) })

    ModalBottomSheet(
        onDismissRequest = { PopupController.dismissSaveVideoToPlaylistModal() },
    ) {
        LazyColumn(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    IconButton(
                        onClick = viewModel::showCreatePlaylistDialog,
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
                            viewModel.saveVideoToPlaylists()
                        }
                    ) {
                        Text(stringResource(R.string.done), fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

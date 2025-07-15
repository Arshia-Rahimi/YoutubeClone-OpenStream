package com.github.openstream.ui.feature.library.root

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.openstream.R
import com.github.openstream.core.shared.dataitem.PlaylistItem
import com.github.openstream.ui.designsystem.components.dataitem.DataItemList
import com.github.openstream.ui.global.reusable.popups.PopupController
import com.github.openstream.ui.global.reusable.popups.inputdialog.model.InputType
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    toChannelScreen: (String) -> Unit,
    toPlaylistScreen: (PlaylistItem) -> Unit,
) {
    val viewModel = koinViewModel<LibraryViewModel>()
    val playlists = viewModel.playlists
    val sortType by viewModel.sortType.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.library))
                },
                actions = {
                    IconButton(
                        onClick = { PopupController.openInputDialog(InputType.CreatePlaylist()) },
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.plus),
                            contentDescription = stringResource(R.string.create_new_playlist)
                        )
                    }
                    IconButton(
                        onClick = { PopupController.openInputDialog(InputType.SearchPlaylist()) },
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.link),
                            contentDescription = stringResource(R.string.search_for_playlist)
                        )
                    }
                }
            )
        }
    ) { ip ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(ip),
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
                    painter = painterResource(R.drawable.sort),
                    contentDescription = stringResource(sortType.string),
                    tint = Color.White,
                )
            }
            DataItemList(
                items = playlists,
                toChannelScreen = toChannelScreen,
                toPlaylistScreen = toPlaylistScreen,
            )
        }
    }
}


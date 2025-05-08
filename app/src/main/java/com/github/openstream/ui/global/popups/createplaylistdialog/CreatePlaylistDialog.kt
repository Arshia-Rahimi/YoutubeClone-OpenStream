package com.github.openstream.ui.global.popups.createplaylistdialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.openstream.R
import com.github.openstream.ui.designsystem.components.OpenStreamDialog
import com.github.openstream.ui.global.popups.PopupController
import org.koin.androidx.compose.koinViewModel

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePlaylistDialog() {
    val viewModel = koinViewModel<CreatePlaylistViewModel>()
    var newPlaylistTitle by remember { mutableStateOf("") }
    
    OpenStreamDialog(
        dismiss = { PopupController.dismissCreatePlaylistDialog() },
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
                onValueChange = { newPlaylistTitle = it },
                singleLine = true,
                keyboardActions = KeyboardActions(
                    onDone = {
                        viewModel.createPlaylist(newPlaylistTitle)
                        PopupController.dismissCreatePlaylistDialog()
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
                    onClick = { PopupController.dismissCreatePlaylistDialog() },
                ) {
                    Text(stringResource(R.string.dismiss))
                }
                Button(
                    onClick = {
                        viewModel.createPlaylist(newPlaylistTitle)
                    },
                ) {
                    Text(stringResource(R.string.create))
                }
            }
        }
    }
}

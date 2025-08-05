package com.github.openstream.ui.feature.settings.root

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.openstream.R
import com.github.openstream.ui.designsystem.components.OpenStreamDialog
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    toLogScreen: () -> Unit,
) {
    val viewModel = koinViewModel<SettingsViewModel>()
    var showCookiesDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar({ Text(stringResource(R.string.settings)) })
        }
    ) { ip ->
        LazyColumn(
            modifier = Modifier
                .padding(ip)
                .fillMaxSize(),
        ) {
            settingsItem(
                title = "log screen",
                action = {
                    Button(
                        onClick = toLogScreen,
                    ) {
                        Text("view logs", fontSize = 12.sp)
                    }
                }
            )
            settingsItem(
                title = "set custom cookies",
                action = {
                    Button(
                        onClick = { showCookiesDialog = true },
                    ) {
                        Text("set", fontSize = 12.sp)
                    }
                }
            )
            settingsItem(
                title = "clear local video history",
                action = {
                    Button(
                        onClick = viewModel::clearLocalVideoHistory,
                        enabled = !viewModel.localVideoHistoryLoading,
                    ) {
                        if (viewModel.localVideoHistoryLoading) CircularProgressIndicator()
                        else Text("clear", fontSize = 12.sp)
                    }
                }
            )
            settingsItem(
                title = "clear watch history",
                action = {
                    Button(
                        onClick = viewModel::clearWatchHistory,
                        enabled = !viewModel.clearWatchHistoryLoading,
                    ) {
                        if (viewModel.clearWatchHistoryLoading) CircularProgressIndicator()
                        else Text("clear", fontSize = 12.sp)
                    }
                }
            )
            settingsItem(
                title = "clear log",
                action = {
                    Button(
                        onClick = viewModel::clearLog,
                        enabled = !viewModel.clearLogLoading,
                    ) {
                        if (viewModel.clearLogLoading) CircularProgressIndicator()
                        else Text("clear", fontSize = 12.sp)
                    }
                }
            )
            settingsItem(
                title = "clear all cache",
                action = {
                    Button(
                        onClick = viewModel::clearCache,
                        enabled = !viewModel.clearCacheLoading,
                    ) {
                        if (viewModel.clearCacheLoading) CircularProgressIndicator()
                        else Text("clear", fontSize = 12.sp)
                    }
                }
            )
        }
        
        if (showCookiesDialog) {
            OpenStreamDialog(
                dismiss = { showCookiesDialog = false },
            ) {
                var cookies: String? by remember { mutableStateOf(null) }
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
                        value = cookies ?: "",
                        onValueChange = { cookies = it },
                        singleLine = true,
                        keyboardActions = KeyboardActions(
                            onDone = {
                                cookies?.let {
                                    viewModel.setCookies(it)
                                    showCookiesDialog = false
                                }
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
                        horizontalArrangement = Arrangement.spacedBy(
                            4.dp,
                            Alignment.CenterHorizontally
                        ),
                    ) {
                        Button(
                            onClick = { showCookiesDialog = false },
                        ) {
                            Text(stringResource(R.string.dismiss))
                        }
                        Button(
                            onClick = {
                                cookies?.let {
                                    viewModel.setCookies(it)
                                    showCookiesDialog = false
                                }
                            },
                        ) {
                            Text("confirm")
                        }
                    }
                }
            }
        }
    }
}

private fun LazyListScope.settingsItem(
    title: String,
    action: @Composable () -> Unit,
) {
    item {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 12.dp)
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
            )
            action()
        }
    }
}

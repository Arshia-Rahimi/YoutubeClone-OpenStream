package com.github.openstream.ui.feature.settings.root

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.openstream.R
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    toLogScreen: () -> Unit,
) {
    val viewModel = koinViewModel<SettingsViewModel>()
    
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

package com.github.arshiarahimi.openstream.ui.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen() {
    val viewModel = koinViewModel<SettingsViewModel>()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { ip ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(ip),
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
                        text = "clear local video history",
                        fontSize = 16.sp,
                    )
                    Button(
                        onClick = viewModel::clearLocalVideoHistory,
                        enabled = !viewModel.localVideoHistoryLoading,
                        modifier = Modifier.fillMaxHeight(),
                    ) {
                        if (viewModel.localVideoHistoryLoading) CircularProgressIndicator()
                        else Text("clear", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

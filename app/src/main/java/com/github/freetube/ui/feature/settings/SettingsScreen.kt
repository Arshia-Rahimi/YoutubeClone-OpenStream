package com.github.freetube.ui.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.freetube.ui.designsystem.LibreTubeContainer
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
) {
    val viewModel = koinViewModel<SettingsScreenViewModel>()
    val settingsData by viewModel.settingsData.collectAsStateWithLifecycle()
    val appTheme = settingsData.appTheme
    LibreTubeContainer(
        inSettingsScreen = true,
        title = "Settings",
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(
                space = 12.dp,
                alignment = Alignment.Top,
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .clickable {
                            viewModel.changeAppTheme()
                        }
                        .padding(vertical = 20.dp, horizontal = 15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text("theme: ")
                    Text(appTheme.name)
                }
            }
        }
    }
}

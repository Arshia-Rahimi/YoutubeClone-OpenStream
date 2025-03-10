package com.github.freetube.ui.feature.home.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.freetube.ui.designsystem.LibreTubeTopBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HomeScreenTopBar(
    showSearchBar: Boolean,
    setShowSearchBar: (Boolean) -> Unit,
    drawerState: DrawerState,
    scope: CoroutineScope,
    searchText: String,
    setSearchText: (String) -> Unit,
) {
    AnimatedContent(showSearchBar) {
        if (it) {
            LibreTubeTopBar(
                title = {},
            ) {
                TextField(
                    singleLine = true,
                    value = searchText,
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .weight(1f),
                    onValueChange = { setSearchText(it) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedTextColor = Color.Transparent,
                        errorContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                    )
                )
                IconButton(
                    onClick = { setShowSearchBar(false) }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "clear",
                    )
                }
            }
        } else {
            LibreTubeTopBar(
                title = { Text("LibreTube") },
                actions = {
                    IconButton(
                        onClick = { setShowSearchBar(true) }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "search",
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { scope.launch { drawerState.open() } },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "navigation drawer",
                        )
                    }
                }
            )
        }
    }
}

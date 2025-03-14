package com.github.freetube.ui.feature.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(

) {
    val viewModel = koinViewModel<SearchScreenViewModel>()
    var query by remember { mutableStateOf("") }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) { 
        item {
            TextField(
                value = query,
                onValueChange = { query = it },
            )
        }
        item { 
            Button(onClick = {viewModel.search(query)})  { 
                Text("search")
            }
        }
    }
}

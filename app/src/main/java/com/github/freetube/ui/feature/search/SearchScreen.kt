package com.github.freetube.ui.feature.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(

) {
    val viewModel = koinViewModel<SearchScreenViewModel>()
    var searchQuery by viewModel.searchQuery
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) { 
        item {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
            )
        }
        item { 
            Button(onClick = {viewModel.search()})  {
                Text("search")
            }
        }
        item {
            Button(onClick = {viewModel.getNextPage() })  {
                Text("nextpage")
            }
        }
    }
}

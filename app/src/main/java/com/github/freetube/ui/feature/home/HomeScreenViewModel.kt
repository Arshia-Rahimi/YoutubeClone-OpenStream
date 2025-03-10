package com.github.freetube.ui.feature.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class HomeScreenViewModel: ViewModel() {
    
    val searchText = mutableStateOf("")
    
}

package com.github.freetube.ui.designsystem.scaffold

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalMaterial3Api::class)
class ScaffoldScreenModel : ScreenModel {
    //    val scrollBehavior
//        @Composable get() = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val topBar: MutableStateFlow<(@Composable () -> Unit)> = MutableStateFlow({})
}

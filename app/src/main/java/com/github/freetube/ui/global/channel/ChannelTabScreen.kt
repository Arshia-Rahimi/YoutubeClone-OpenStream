package com.github.freetube.ui.global.channel

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.freetube.app.navigation.LibreTubeScreen
import com.github.freetube.ui.designsystem.scaffold.ScaffoldScreenModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

data class ChannelTabScreen(
    private val url: String,
) : LibreTubeScreen() {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = koinScreenModel<ChannelScreenModel> { parametersOf(url) }
        val scaffoldScreenModel = koinInject<ScaffoldScreenModel>()
        ChannelScreen(
            screenModel = screenModel,
            topBar = { scaffoldScreenModel.topBar.value = it },
            navigateBack = { navigator.pop() },
        )
    }
}

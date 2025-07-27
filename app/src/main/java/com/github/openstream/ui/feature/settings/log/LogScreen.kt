package com.github.openstream.ui.feature.settings.log

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.openstream.R
import com.github.openstream.core.shared.MiniPlayerConfig
import com.github.openstream.ui.global.player.PlayerViewModel
import org.koin.androidx.compose.koinViewModel

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogScreen(
    navigateBack: () -> Unit,
) {
    val viewModel = koinViewModel<LogViewModel>()
    val log by viewModel.log.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    var isFirstScroll by remember { mutableStateOf(true) }
    
    val playerViewModel = koinViewModel<PlayerViewModel>()
    val showMiniPlayer by playerViewModel.showMiniPlayer.collectAsStateWithLifecycle()
    
    LaunchedEffect(log) {
        if (isFirstScroll) {
            scrollState.scrollTo(scrollState.maxValue)
            isFirstScroll = false
        } else {
            scrollState.animateScrollTo(scrollState.maxValue)
        }
    }
    
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.log)) },
                navigationIcon = {
                    IconButton(
                        onClick = navigateBack,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "back",
                        )
                    }
                }
            )
        }
    ) { ip ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(ip),
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState),
                text = log,
                fontSize = 8.sp,
                color = Color.White,
                lineHeight = TextUnit(10f, TextUnitType.Sp),
            )
            if (showMiniPlayer) {
                val localConfig = LocalConfiguration.current
                val widthToScreenWidthRatio =
                    if (localConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) MiniPlayerConfig.LANDSCAPE_WIDTH_TO_SCREEN_WIDTH_RATIO
                    else MiniPlayerConfig.WIDTH_TO_SCREEN_WIDTH_RATIO
                Spacer(
                    Modifier
                        .fillMaxWidth()
                        .height((localConfig.screenWidthDp * widthToScreenWidthRatio * 9 / 16).dp)
                )
            }
        }
    }
}

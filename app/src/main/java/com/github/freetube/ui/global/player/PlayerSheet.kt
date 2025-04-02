package com.github.freetube.ui.global.player

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arshia.freetube.R
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun BoxScope.PlayerSheet(
    viewModel: PlayerViewModel,
    toChannelScreen: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentPosition by viewModel.currentPosition.collectAsStateWithLifecycle()
    val playerState by viewModel.playerState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    val config = LocalConfiguration.current
    val screenHeight = config.screenHeightDp.dp
    val screenWidth = config.screenWidthDp.dp.value
    var bottomSheetHeight: Dp
    var progress = 1f
//    var playerWidth: MutableState<Float>

    LaunchedEffect(1) {
        while (true) {
            if (sheetState.currentValue == SheetValue.Hidden) return@LaunchedEffect
            bottomSheetHeight = screenHeight - sheetState.requireOffset().roundToInt().dp
            progress = (bottomSheetHeight.value / screenHeight.value + 0.37f) * 0.73f
//        playerWidth = animateFloatAsState(progress * screenWidth) 
        }
    }

    ModalBottomSheet(
        onDismissRequest = { scope.launch { sheetState.partialExpand() } },
        sheetState = sheetState,
        dragHandle = {
            Box(
                modifier = Modifier
                    .systemBarsPadding()
                    .width((progress * screenWidth).dp)
                    .aspectRatio(16 / 9f),
            ) {
                Icon(
                    modifier = Modifier.matchParentSize(),
                    painter = painterResource(R.drawable.subs_selected),
                    contentDescription = null,
                )
            }
        },
    ) {
        Box(Modifier.fillMaxSize())
    }
}

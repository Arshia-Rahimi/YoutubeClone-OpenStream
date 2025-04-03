package com.github.freetube.ui.global.player

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arshia.freetube.R
import com.skydoves.flexible.bottomsheet.material3.FlexibleBottomSheet
import com.skydoves.flexible.core.FlexibleSheetSize
import com.skydoves.flexible.core.FlexibleSheetValue
import com.skydoves.flexible.core.rememberFlexibleBottomSheetState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun PlayerSheet(
    viewModel: PlayerViewModel,
    toChannelScreen: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentPosition by viewModel.currentPosition.collectAsStateWithLifecycle()
    val playerState by viewModel.playerState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val sheetState = rememberFlexibleBottomSheetState(
        isModal = false,
        skipIntermediatelyExpanded = true,
        skipSlightlyExpanded = false,
        skipHiddenState = true,
        containSystemBars = true,
        flexibleSheetSize = FlexibleSheetSize(
            fullyExpanded = 1f,
            slightlyExpanded = 0.1f,
        ),
    )
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp.dp.value
    val screenHeight = config.screenHeightDp
    val slightlyExpandedPlayerWidth = screenWidth * 0.2f
    var sheetValue by remember { mutableStateOf(FlexibleSheetValue.SlightlyExpanded) }
    var sheetHeight by remember { mutableFloatStateOf(slightlyExpandedPlayerWidth) }
    println(screenHeight)
    val playerWidth by remember {
        derivedStateOf {
            when (sheetState.currentValue) {
                FlexibleSheetValue.SlightlyExpanded -> slightlyExpandedPlayerWidth
                else -> sheetHeight / screenHeight
            }
        }
    }
    val animatedPlayerWidth by animateFloatAsState(playerWidth)


    FlexibleBottomSheet(
        scrimColor = MaterialTheme.colorScheme.background,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.background,
        onDismissRequest = { scope.launch { sheetState.slightlyExpand() } },
        sheetState = sheetState,
        onTargetChanges = { sheetValue = it },
        dragHandle = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned {
                        sheetHeight = it.positionInRoot().y
                    },
                horizontalArrangement = Arrangement.Start,
            ) {
                Box(
                    modifier = Modifier
                        .systemBarsPadding()
                        .width(animatedPlayerWidth.dp)
                        .aspectRatio(16 / 9f),
                ) {
                    Icon(
                        modifier = Modifier.matchParentSize(),
                        painter = painterResource(R.drawable.subs_selected),
                        contentDescription = null,
                    )
                }
            }
        },

        ) {

    }
}

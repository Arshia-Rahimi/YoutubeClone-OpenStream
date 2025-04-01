package com.github.freetube.ui.global.player

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.PlayerSheet(
    viewModel: PlayerViewModel,
    toChannelScreen: (String) -> Unit,
    sheetValue: SheetValue,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sheetState: SheetState,
    columnScope: ColumnScope,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentPosition by viewModel.currentPosition.collectAsStateWithLifecycle()
    val playerState by viewModel.playerState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    with(columnScope) {
        when (sheetValue) {
            SheetValue.Hidden -> {}
            SheetValue.Expanded -> {}

            SheetValue.PartiallyExpanded -> {
                Box(
                    modifier = Modifier
                        .weight(0.1f)
                        .clickable { scope.launch { sheetState.expand() } },
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.sharedElement(
                            state = rememberSharedContentState(LOADING_SHARED_KEY),
                            animatedVisibilityScope = animatedVisibilityScope,
                        )
                    )
                }
            }
        }
    }
}

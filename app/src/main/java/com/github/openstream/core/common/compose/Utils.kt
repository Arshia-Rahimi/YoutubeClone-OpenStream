package com.github.openstream.core.common.compose

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun <T> Flow<List<T>>.collectToSnapShotStateList(
    scope: CoroutineScope,
    sort: (List<T>) -> List<T> = { it },
) =
    mutableStateListOf<T>().apply {
        this@collectToSnapShotStateList.onEach { items ->
            val sortedItems = sort(items)
            clear()
            addAll(sortedItems)
        }.launchIn(scope)
    }

enum class Orientation(val data: Int) {
    Portrait(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT),
    LandScape(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE),
}

fun changeOrientation(activity: Activity, orientation: Orientation) {
    activity.requestedOrientation = orientation.data
}

@Composable
fun ChangeOrientationOnBackButton(orientation: Orientation) {
    LocalActivity.current?.let {
        BackHandler {
            changeOrientation(it, orientation)
        }
    }
}

fun AnchoredDraggableState<*>.safeOffset() = if (offset.isNaN()) 0f else offset

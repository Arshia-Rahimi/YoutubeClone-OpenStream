package com.github.freetube.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.github.freetube.core.model.LibreTubeData

@Composable
fun rememberLibreTubeAppState(
    libreTubeData: LibreTubeData,
    navHostController: NavHostController = rememberNavController(),
): LibreTubeAppState = remember {
    LibreTubeAppState(
        libreTubeData = libreTubeData,
        navController = navHostController,
    )
}

data class LibreTubeAppState(
    val libreTubeData: LibreTubeData,
    val navController: NavHostController,
)

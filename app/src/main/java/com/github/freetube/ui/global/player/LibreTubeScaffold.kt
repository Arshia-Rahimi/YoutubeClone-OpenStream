package com.github.freetube.ui.global.player

//noinspection UsingMaterialAndMaterial3Libraries
import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.navigator.tab.Tab
import com.github.freetube.ui.feature.downloads.DownloadsTab
import com.github.freetube.ui.feature.library.LibraryTab
import com.github.freetube.ui.feature.search.SearchTab
import com.github.freetube.ui.feature.settings.SettingsTab
import com.github.freetube.ui.feature.subscriptions.SubscriptionsTab
import org.koin.compose.koinInject

val libreTubeTabs = arrayOf(
    SearchTab,
    LibraryTab,
    SubscriptionsTab,
    DownloadsTab,
    SettingsTab,
)

const val LOADING_SHARED_KEY = ""

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun LibreTubeScaffold(
    currentTab: Tab,
    navigateToTab: (Tab) -> Unit,
    content: @Composable (Modifier) -> Unit,
) {
    val screenModel = koinInject<PlayerScreenModel>()
    val topBar by screenModel.topBar.collectAsStateWithLifecycle()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded,
            skipHiddenState = false,
        )
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomBar(
                currentTab = currentTab,
                navigateToTab = navigateToTab,
            )
        }
    ) {
        SharedTransitionLayout {
            AnimatedContent(bottomSheetScaffoldState.bottomSheetState.currentValue) { state ->
                BottomSheetScaffold(
                    sheetPeekHeight = 150.dp,
                    modifier = Modifier
                        .fillMaxSize(),
                    scaffoldState = bottomSheetScaffoldState,
//                .nestedScroll(screenModel.scrollBehavior.nestedScrollConnection),
                    topBar = { topBar() },
                    snackbarHost = {
//                SnackbarHost(
//                    hostState = snackbarHostState,
//                )
                    },
                    sheetDragHandle = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.sharedElement(
                                    state = rememberSharedContentState(LOADING_SHARED_KEY),
                                    animatedVisibilityScope = this@AnimatedContent,
                                )
                            )
                        }
                    },
                    sheetContent = {
                        PlayerSheet(
                            screenModel = screenModel,
                            toChannelScreen = {
                                // todo
                            },
                            sheetValue = state,
                            animatedVisibilityScope = this@AnimatedContent,
                            sheetState = bottomSheetScaffoldState.bottomSheetState,
                            columnScope = this,
                        )
                    },
                ) { ip ->
                    content(
                        Modifier
                            .fillMaxSize()
                            .padding(ip)
                            .consumeWindowInsets(ip)
                            .windowInsetsPadding(
                                WindowInsets.safeDrawing.only(
                                    WindowInsetsSides.Horizontal
                                ),
                            ),
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomBar(
    currentTab: Tab,
    navigateToTab: (Tab) -> Unit,
) {
    BottomAppBar(
        contentColor = Color(0xFF1A1A1A),
        containerColor = Color(0xFF1A1A1A),
    ) {
        libreTubeTabs.forEach { tab ->
            val selected = currentTab == tab
            // todo migrate to m3
            BottomNavigationItem(
                modifier = Modifier.navigationBarsPadding(),
                selected = selected,
                onClick = { navigateToTab(tab) },
                icon = {
                    Icon(
                        painter = painterResource(
                            if (selected) tab.data.selectedIcon else tab.data.icon
                        ),
                        contentDescription = null,
                        tint = Color.Unspecified,
                    )
                },
                label = {
                    Text(
                        text = tab.options.title,
                        maxLines = 1,
                        fontSize = 8.sp,
                        color = if (selected) Color.White else Color(0xFFBABABA)
                    )
                },
            )
        }
    }
}

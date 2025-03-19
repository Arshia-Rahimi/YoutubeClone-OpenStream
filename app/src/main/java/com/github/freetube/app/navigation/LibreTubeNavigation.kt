package com.github.freetube.app.navigation

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun LibreTubeNavigation() {
//    val appState = rememberLibreTubeAppState()
//    val currentTLD by appState.currentTLD.collectAsStateWithLifecycle()
//    val navController = appState.baseNavController
//    var showBottomSheet by remember { mutableStateOf(false) }
//
//    LibreTubeScaffold(
//        appState = appState,
//        currentTLD = currentTLD,
//    ) { modifier ->
//        Box(
//            modifier = modifier,
//        ) {
//            NavHost(
//                navController = navController,
//                startDestination = LibreTubeRoutes.Subscriptions,
//                route = LibreTubeRoutes::class,
//            ) {
//                composable<LibreTubeRoutes.Subscriptions>(
//                ) {
//                    val subscriptionsNavController = rememberNavController()
//                    SubscriptionsScreenNavigation(subscriptionsNavController)
//                }
//                composable<LibreTubeRoutes.Settings> {
//                    val settingsNavController = rememberNavController()
//                    SettingsScreenNavigation(settingsNavController)
//                }
//                composable<LibreTubeRoutes.Library> {
//                    val libraryNavController = rememberNavController()
//                    LibraryScreenNavigation(libraryNavController)
//                }
//                composable<LibreTubeRoutes.Downloads> {
//                    val downloadsNavController = rememberNavController()
//                    DownloadsScreenNavigation(downloadsNavController)
//                }
//                composable<LibreTubeRoutes.Search> {
//                    val searchNavController = rememberNavController()
//                    SearchScreenNavigation(searchNavController)
//                }
//            }
////            NavHost(
////                modifier = Modifier.fillMaxSize(),
////                navController = navController,
////                startDestination = LibreTubeRoutes.Subscriptions
////            ) {
////                subscriptionsScreenNavigation(
////                    toSearchScreen = { appState.navigateToTopLevelDestination(TopLevelDestinations.Search) },
////                    toSubsPlaylistScreen = { navController.navigate(LibreTubeRoutes.Subscriptions.Playlist) }
////                )
////                settingsScreenNavigation()
////                libraryScreenNavigation()
////                searchScreenNavigation(
////                    navController = navController,
////                    toSubsScreen = { appState.navigateToTopLevelDestination(TopLevelDestinations.Subscriptions) },
////                    toSubsChannelScreen = { navController.navigate(LibreTubeRoutes.Subscriptions.Channel) }
////                )
////                downloadsScreenNavigation()
////            }
//            MiniPlayer(
//                modifier = Modifier.align(Alignment.BottomCenter),
//                showBottomSheet = { showBottomSheet = true }
//            )
//        }
//        if(showBottomSheet) PlayerSheet()
//    }
//}

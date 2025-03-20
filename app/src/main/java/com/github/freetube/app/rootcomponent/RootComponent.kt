package com.github.freetube.app.rootcomponent

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.children.ChildNavState.Status
import com.arkivanov.decompose.router.pages.ChildPages
import com.arkivanov.decompose.router.pages.Pages
import com.arkivanov.decompose.router.pages.PagesNavigation
import com.arkivanov.decompose.router.pages.childPages
import com.arkivanov.decompose.router.pages.select
import com.arkivanov.decompose.value.Value
import com.github.freetube.core.common.util.onFirst
import com.github.freetube.core.data.LibreTubeDataRepository
import com.github.freetube.core.extractor.OkHttpDownloader
import com.github.freetube.ui.designsystem.TabComponent
import com.github.freetube.ui.feature.downloads.DownloadsComponent
import com.github.freetube.ui.feature.library.LibraryComponent
import com.github.freetube.ui.feature.search.SearchComponent
import com.github.freetube.ui.feature.settings.SettingsComponent
import com.github.freetube.ui.feature.subscriptions.SubscriptionsComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.koin.core.component.KoinComponent
import org.schabi.newpipe.extractor.NewPipe
import org.schabi.newpipe.extractor.localization.Localization

interface RootComponent {
    val pages: Value<ChildPages<Tabs, TabComponent>>

    fun selectTab(index: Int)
}

class LibreTubeRootComponent(
    componentContext: ComponentContext,
    scope: CoroutineScope,
) : RootComponent, KoinComponent, ComponentContext by componentContext {
    // koin dependencies
    private val libreTubeDataRepository by getKoin().inject<LibreTubeDataRepository>()
    private val downloader by getKoin().inject<OkHttpDownloader>()

    // app start routine
    val libreTubeData = libreTubeDataRepository.data
        .onFirst { loadNewPipeConfig() }
        .stateIn(
            scope = scope,
            initialValue = null,
            started = SharingStarted.WhileSubscribed(5000L)
        )

    private fun loadNewPipeConfig() =
        // todo get locale and add it to dataStore
        NewPipe.init(downloader, Localization("en", "US"))

    // tab navigation logic
    private val navigation = PagesNavigation<Tabs>()

    override fun selectTab(index: Int) = navigation.select(index)

    override val pages = childPages(
        source = navigation,
        serializer = Tabs.serializer(),
        initialPages = {
            Pages(
                items = Tabs.entries,
                selectedIndex = 2,
            )
        },
        pageStatus = { _, _ -> Status.RESUMED },
        childFactory = ::childFactory,
    )

    private fun childFactory(
        tab: Tabs,
        context: ComponentContext,
    ): TabComponent = when (tab) {
        Tabs.Settings -> SettingsComponent(context)
        Tabs.Downloads -> DownloadsComponent(context)
        Tabs.Subscriptions -> SubscriptionsComponent(context)
        Tabs.Library -> LibraryComponent(context)
        Tabs.Search -> SearchComponent(context)
    }
}

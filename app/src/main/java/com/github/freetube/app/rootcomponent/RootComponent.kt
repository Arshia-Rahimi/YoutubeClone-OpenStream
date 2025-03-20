package com.github.freetube.app.rootcomponent

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.pages.ChildPages
import com.arkivanov.decompose.router.pages.Pages
import com.arkivanov.decompose.router.pages.PagesNavigation
import com.arkivanov.decompose.router.pages.childPages
import com.arkivanov.decompose.router.pages.select
import com.arkivanov.decompose.value.Value
import com.github.freetube.app.rootcomponent.RootComponent.Child
import com.github.freetube.core.common.util.onFirst
import com.github.freetube.core.data.LibreTubeDataRepository
import com.github.freetube.core.extractor.OkHttpDownloader
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
    val pages: Value<ChildPages<Tabs, Child>>

    fun selectTab(index: Int)

    sealed class Child {
        data class SettingsChild(val component: SettingsComponent) : Child()
        data class DownloadsChild(val component: DownloadsComponent) : Child()
        data class SearchChild(val component: SearchComponent) : Child()
        data class LibraryChild(val component: LibraryComponent) : Child()
        data class SubscriptionsChild(val component: SubscriptionsComponent) : Child()
    }
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
//        pageStatus = { _, _ -> Status.RESUMED },
        childFactory = ::childFactory,
    )

    private fun childFactory(
        tab: Tabs,
        context: ComponentContext,
    ): Child = when (tab) {
        Tabs.Settings -> Child.SettingsChild(SettingsComponent(context))
        Tabs.Downloads -> Child.DownloadsChild(DownloadsComponent(context))
        Tabs.Subscriptions -> Child.SubscriptionsChild(SubscriptionsComponent(context))
        Tabs.Library -> Child.LibraryChild(LibraryComponent(context))
        Tabs.Search -> Child.SearchChild(SearchComponent(context))
    }
}

package com.github.freetube.app

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.pages.ChildPages
import com.arkivanov.decompose.router.pages.PagesNavigation
import com.arkivanov.decompose.value.Value
import com.github.freetube.app.RootComponent.Child
import com.github.freetube.ui.feature.settings.SettingsComponent
import kotlinx.serialization.Serializable

interface RootComponent {
    val pages: Value<ChildPages<*, PageComponent>>

    sealed class Child {
        data class Settings(val component: SettingsComponent) : Child()
        data class Downloads(val component: DefaultRootComponent) : Child()
        data class Library
        data class Subscriptions
        data class Search
    }

    @Serializable
    sealed class Config {
        @Serializable
        data object Settings

        @Serializable
        data object Search

        @Serializable
        data object Downloads

        @Serializable
        data object Subscriptions

        @Serializable
        data object Library
    }
}

class DefaultRootComponent(
    componentContext: ComponentContext,
) : RootComponent, ComponentContext by componentContext {

    private val navigation = PagesNavigation<Config>()

//    override val pages = childPages(
//        source = navigation,
//        serializer = Config.serializer(),
//        initialPages = {
//            Pages(
//                items = TopLevelDestinations.entries,
//                selectedIndex = 2,
//            )
//        },
//        pageStatus = { ChildNavState.Status.RESUMED },
//        childFactory = {},
//    ) { config, childComponentContext ->
//        when (config) {
//
//        }
//    }

    private fun childFactory(
        config: RootComponent.Config,
        context: ComponentContext,
    ) = when (config) {
        RootComponent.Config.Settings -> Child.Settings(
            SettingsComponent(
                componentContext = context,
            )
        )
    }

}

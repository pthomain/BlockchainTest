package dev.pthomain.skeleton.ui.features.main.di

import dev.pthomain.skeleton.App
import dev.pthomain.skeleton.di.ComponentProvider
import dev.pthomain.skeleton.ui.features.main.MainFragment
import dev.pthomain.skeleton.ui.shared.interactors.di.SharedModule

class MainComponentProvider(
    private val mainFragment: MainFragment
) : ComponentProvider<MainComponent> {

    override fun getComponent() =
        DaggerMainComponent
            .builder()
            .appComponent(App.component)
            .sharedModule(SharedModule(mainFragment.context!!))
            .mainModule(MainModule(mainFragment.context!!, mainFragment))
            .build()

}

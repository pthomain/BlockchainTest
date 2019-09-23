package dev.pthomain.skeleton.ui.features.main.base

import dev.pthomain.skeleton.App
import dev.pthomain.skeleton.base.integration.components.test.TestComponentProvider
import dev.pthomain.skeleton.di.ComponentProvider
import dev.pthomain.skeleton.ui.features.main.MainFragment
import dev.pthomain.skeleton.ui.shared.interactors.di.SharedModule

class MainTestComponentProvider(
    private val mainFragment: MainFragment
) : ComponentProvider<MainTestComponent> {

    override fun getComponent() =
        DaggerMainTestComponent.builder()
            .testComponent(TestComponentProvider().getComponent())
            .mainTestModule(
                MainTestModule(
                    App.instance,
                    mainFragment
                )
            ).sharedModule(SharedModule((App.instance)))
            .build()

}

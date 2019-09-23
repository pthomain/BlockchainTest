package dev.pthomain.skeleton.ui.features.main.base

import com.nhaarman.mockitokotlin2.mock
import dev.pthomain.skeleton.App
import dev.pthomain.skeleton.base.integration.components.test.TestComponentProvider
import dev.pthomain.skeleton.di.ComponentProvider
import dev.pthomain.skeleton.ui.features.main.MainFragment
import dev.pthomain.skeleton.ui.shared.interactors.di.SharedModule
import org.robolectric.util.FragmentTestUtil.startFragment

class MainTestComponentProvider : ComponentProvider<MainTestComponent> {

    override fun getComponent() = DaggerMainTestComponent.builder()
        .testComponent(TestComponentProvider().getComponent())
        .mainTestModule(
            MainTestModule(
                App.instance,
                mock<MainFragment>().apply { startFragment(this) })
        )
        .sharedModule(SharedModule((App.instance)))
        .build()

}

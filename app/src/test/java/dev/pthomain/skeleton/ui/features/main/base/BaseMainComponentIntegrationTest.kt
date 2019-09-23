package dev.pthomain.skeleton.ui.features.main.base

import androidx.fragment.app.testing.launchFragmentInContainer
import dev.pthomain.skeleton.base.integration.BaseIntegrationTest
import dev.pthomain.skeleton.ui.features.main.MainFragment

abstract class BaseMainComponentIntegrationTest<T : Any>(
    targetExtractor: (MainTestComponent) -> T
) : BaseIntegrationTest<MainTestComponent, T>(
    MainTestComponentProvider(getMainFragment()()),
    targetExtractor
)

private fun getMainFragment(): () -> MainFragment {
    val holder = MainFragmentHolder()

    val scenario = launchFragmentInContainer(
        instantiate = {
            MainFragment { MainTestComponentProvider(it) }
        }
    )

    scenario.recreate()
    scenario.onFragment { holder.mainFragment = it }

    return holder
}

private class MainFragmentHolder : () -> MainFragment {
    lateinit var mainFragment: MainFragment
    override fun invoke() = mainFragment
}
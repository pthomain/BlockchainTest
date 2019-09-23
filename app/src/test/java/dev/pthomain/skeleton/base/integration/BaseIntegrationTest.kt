package dev.pthomain.skeleton.base.integration

import androidx.annotation.CallSuper
import androidx.test.core.app.ApplicationProvider
import dev.pthomain.skeleton.di.ComponentProvider
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
abstract class BaseIntegrationTest<C : Any, T : Any>(
    private val componentProvider: ComponentProvider<C>,
    private val targetExtractor: (C) -> T
) {

    protected lateinit var component: C
    protected lateinit var target: T

    @Before
    @CallSuper
    open fun setUp() {
        component = componentProvider.getComponent()
        target = targetExtractor(component)
    }

}


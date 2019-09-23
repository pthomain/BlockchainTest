package dev.pthomain.skeleton.ui.di

import android.os.Bundle
import dev.pthomain.skeleton.di.ComponentProvider
import uk.co.glass_software.android.boilerplate.core.base.fragment.BaseFragment

abstract class BaseInjectedFragment<C : Any, T : BaseInjectedFragment<C, T>>(
    private val componentProvider: (T) -> ComponentProvider<C>
) : BaseFragment() {

    protected lateinit var component: C
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component = componentProvider(this as T).getComponent()
    }
}
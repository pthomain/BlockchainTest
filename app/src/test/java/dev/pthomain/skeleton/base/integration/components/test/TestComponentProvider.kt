package dev.pthomain.skeleton.base.integration.components.test

import androidx.test.core.app.ApplicationProvider
import dev.pthomain.skeleton.base.di.DaggerTestComponent
import dev.pthomain.skeleton.base.di.TestComponent
import dev.pthomain.skeleton.base.di.TestModule
import dev.pthomain.skeleton.di.ComponentProvider
import dev.pthomain.skeleton.domain.di.DomainModule

class TestComponentProvider : ComponentProvider<TestComponent> {

    override fun getComponent() =
        DaggerTestComponent.builder()
            .testModule(TestModule(ApplicationProvider.getApplicationContext()))
            .domainModule(DomainModule(ApplicationProvider.getApplicationContext()))
            .build()

}
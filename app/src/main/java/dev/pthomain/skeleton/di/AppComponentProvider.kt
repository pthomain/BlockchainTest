package dev.pthomain.skeleton.di

import dev.pthomain.skeleton.App
import dev.pthomain.skeleton.domain.di.DomainModule

class AppComponentProvider(private val app: App) : ComponentProvider<AppComponent> {

    override fun getComponent() =
        DaggerAppComponent
            .builder()
            .domainModule(DomainModule(app))
            .build()

}

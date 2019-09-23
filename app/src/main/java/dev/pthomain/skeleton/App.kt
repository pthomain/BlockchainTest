package dev.pthomain.skeleton

import android.app.Application
import dev.pthomain.skeleton.di.AppComponent
import dev.pthomain.skeleton.di.AppComponentProvider
import dev.pthomain.skeleton.di.DaggerAppComponent
import dev.pthomain.skeleton.domain.di.DomainModule
import io.reactivex.plugins.RxJavaPlugins
import uk.co.glass_software.android.boilerplate.core.HasLogger
import uk.co.glass_software.android.boilerplate.core.utils.log.Logger
import uk.co.glass_software.android.boilerplate.core.utils.log.SimpleLogger

class App : Application() {

    companion object {
        lateinit var instance: App
            private set

        lateinit var component: AppComponent
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        component = AppComponentProvider(this).getComponent()

        RxJavaPlugins.setErrorHandler {
            component.logger().e(
                this,
                it,
                "Uncaught RxJava exception"
            )
        }
    }

}
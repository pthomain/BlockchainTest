package dev.pthomain.skeleton

import android.app.Application
import dev.pthomain.skeleton.di.AppComponent
import dev.pthomain.skeleton.di.AppComponentProvider
import io.reactivex.plugins.RxJavaPlugins

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
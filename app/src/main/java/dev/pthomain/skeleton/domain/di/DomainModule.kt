package dev.pthomain.skeleton.domain.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dev.pthomain.skeleton.BuildConfig
import uk.co.glass_software.android.boilerplate.core.utils.log.Logger
import uk.co.glass_software.android.boilerplate.core.utils.log.SimpleLogger
import javax.inject.Singleton

@Module
class DomainModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideContext() =
        context.applicationContext

    @Provides
    @Singleton
    fun provideLogger(): Logger =
        SimpleLogger(
            BuildConfig.DEBUG,
            context.packageName
        )

}

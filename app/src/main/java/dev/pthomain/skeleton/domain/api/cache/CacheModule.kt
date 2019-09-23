package dev.pthomain.skeleton.domain.api.cache

import android.content.Context
import dagger.Module
import dagger.Provides
import dev.pthomain.skeleton.domain.cache.CacheStatisticsComposer
import dev.pthomain.skeleton.domain.di.DomainModule
import dev.pthomain.skeleton.domain.serialisation.di.SerialisationModule
import dev.pthomain.skeleton.domain.serialisation.gson.GsonSerialiser
import uk.co.glass_software.android.boilerplate.core.utils.log.Logger
import uk.co.glass_software.android.dejavu.DejaVu
import uk.co.glass_software.android.dejavu.interceptors.internal.error.Glitch
import javax.inject.Singleton

@Module(
    includes = [
        DomainModule::class,
        SerialisationModule::class
    ]
)
class CacheModule {

    @Provides
    @Singleton
    fun provideDejaVu(
        context: Context,
        logger: Logger,
        gsonSerialiser: GsonSerialiser
    ) =
        //personal lib for caching, see https://github.com/pthomain/dejavu
        DejaVu.builder()
            .logger(logger)
            .encryption { NoOpEncryptionManager() }
            .encryptByDefault(false)
            .compressByDefault(false)
            .build(context, gsonSerialiser)

    @Provides
    @Singleton
    fun provideCacheStatisticsComposerFactory(
        dejaVu: DejaVu<Glitch>,
        logger: Logger
    ) =
        CacheStatisticsComposer.Factory(
            dejaVu,
            logger
        )

}

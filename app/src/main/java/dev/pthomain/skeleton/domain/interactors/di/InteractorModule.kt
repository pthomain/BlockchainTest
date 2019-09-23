package dev.pthomain.skeleton.domain.interactors.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dev.pthomain.skeleton.domain.api.cache.CacheEndpoints
import dev.pthomain.skeleton.domain.api.cache.CacheModule
import dev.pthomain.skeleton.domain.api.di.ApiModule
import dev.pthomain.skeleton.domain.api.multi_address.MultiAddressEndpoints
import dev.pthomain.skeleton.domain.api.multi_address.MultiAddressEndpoints.Companion.CACHE_DURATION_MILLIS
import dev.pthomain.skeleton.domain.api.multi_address.models.MultiAddressResponse
import dev.pthomain.skeleton.domain.cache.CacheStatisticsComposer
import dev.pthomain.skeleton.domain.interactors.cache.CacheMetadataInteractor
import dev.pthomain.skeleton.domain.interactors.cache.ClearCacheInteractor
import dev.pthomain.skeleton.domain.interactors.multi_address.GetMultiAddressInteractor
import uk.co.glass_software.android.dejavu.DejaVu
import uk.co.glass_software.android.dejavu.configuration.CacheInstruction
import uk.co.glass_software.android.dejavu.configuration.CacheInstruction.Operation.DoNotCache
import uk.co.glass_software.android.dejavu.configuration.CacheInstruction.Operation.Expiring.Refresh
import uk.co.glass_software.android.dejavu.interceptors.internal.error.Glitch
import javax.inject.Named
import javax.inject.Singleton

@Module
class InteractorModule {

    @Provides
    @Singleton
    fun provideCacheMetadataInteractorFactory(dejaVu: DejaVu<Glitch>) =
        CacheMetadataInteractor.Factory(dejaVu)

    @Provides
    @Singleton
    fun provideClearCacheInteractor(
        cacheEndpoints: CacheEndpoints,
        cacheStatisticsComposerFactory: CacheStatisticsComposer.Factory
    ) =
        ClearCacheInteractor(
            cacheEndpoints,
            cacheStatisticsComposerFactory
        )

    @Provides
    @Singleton
    fun provideGetMultiAddressInteractor(
        context: Context,
        @Named(REFRESH) refreshInstruction: CacheInstruction,
        @Named(DO_NOT_CACHE) doNotCacheInstruction: CacheInstruction,
        multiAddressEndpoints: MultiAddressEndpoints,
        cacheStatisticsComposerFactory: CacheStatisticsComposer.Factory
    ) =
        GetMultiAddressInteractor.Factory(
            context,
            refreshInstruction,
            doNotCacheInstruction,
            multiAddressEndpoints,
            cacheStatisticsComposerFactory
        )

    @Provides
    @Singleton
    @Named(REFRESH)
    fun provideGetMultiAddressRefreshInstruction(
    ) =
        CacheInstruction(
            MultiAddressResponse::class.java,
            Refresh(durationInMillis = CACHE_DURATION_MILLIS)
        )

    @Provides
    @Singleton
    @Named(DO_NOT_CACHE)
    fun provideGetMultiAddressDoNotCacheInstruction(
    ) =
        CacheInstruction(
            MultiAddressResponse::class.java,
            DoNotCache
        )

    companion object {
        private const val REFRESH = "REFRESH"
        private const val DO_NOT_CACHE = "DO_NOT_CACHE"
    }

}

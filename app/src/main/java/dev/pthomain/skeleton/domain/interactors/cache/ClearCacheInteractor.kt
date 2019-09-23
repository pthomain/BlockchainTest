package dev.pthomain.skeleton.domain.interactors.cache

import dev.pthomain.skeleton.domain.api.cache.CacheEndpoints
import dev.pthomain.skeleton.domain.cache.CacheStatisticsComposer
import dev.pthomain.android.boilerplate.core.interactors.CompletableInteractor

class ClearCacheInteractor(
    private val cacheEndpoints: CacheEndpoints,
    private val cacheStatisticsComposerFactory: CacheStatisticsComposer.Factory
) : CompletableInteractor {

    override fun call() =
        cacheEndpoints.clearCache()
            .compose(cacheStatisticsComposerFactory.create<Any>(this))

}

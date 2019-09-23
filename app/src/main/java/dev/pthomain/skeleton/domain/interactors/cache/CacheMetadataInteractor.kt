package dev.pthomain.skeleton.domain.interactors.cache

import dev.pthomain.android.boilerplate.core.interactors.ObservableInteractor
import dev.pthomain.android.dejavu.DejaVu
import dev.pthomain.android.dejavu.interceptors.internal.error.Glitch
import dev.pthomain.android.dejavu.response.CacheMetadata

class CacheMetadataInteractor private constructor(
    private val dejaVu: DejaVu<Glitch>,
    private val responseClassFilter: Class<*>?
) : ObservableInteractor<CacheMetadata<Glitch>> {

    override fun call() =
        dejaVu.cacheMetadataObservable
            .filter {
                responseClassFilter == null || it.cacheToken.instruction.responseClass == it
            }

    class Factory(private val dejaVu: DejaVu<Glitch>) {

        fun create(responseClassFilter: Class<*>? = null) =
            CacheMetadataInteractor(
                dejaVu,
                responseClassFilter
            )

    }

}

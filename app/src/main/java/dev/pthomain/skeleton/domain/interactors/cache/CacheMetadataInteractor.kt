package dev.pthomain.skeleton.domain.interactors.cache

import uk.co.glass_software.android.boilerplate.core.interactors.ObservableInteractor
import uk.co.glass_software.android.dejavu.DejaVu
import uk.co.glass_software.android.dejavu.interceptors.internal.error.Glitch
import uk.co.glass_software.android.dejavu.response.CacheMetadata

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

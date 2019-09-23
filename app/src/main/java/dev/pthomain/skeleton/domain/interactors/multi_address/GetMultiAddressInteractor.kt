package dev.pthomain.skeleton.domain.interactors.multi_address

import android.content.Context
import dev.pthomain.skeleton.R
import dev.pthomain.skeleton.domain.api.multi_address.MultiAddressEndpoints
import dev.pthomain.skeleton.domain.api.multi_address.models.MultiAddressResponse
import dev.pthomain.skeleton.domain.cache.CacheStatisticsComposer
import io.reactivex.Observable
import dev.pthomain.android.boilerplate.core.interactors.ObservableInteractor
import dev.pthomain.android.boilerplate.core.utils.kotlin.ifElse
import dev.pthomain.android.dejavu.configuration.CacheInstruction
import dev.pthomain.android.dejavu.configuration.CacheInstruction.Operation.Expiring.Refresh

class GetMultiAddressInteractor private constructor(
    private val context: Context,
    private val endpoints: MultiAddressEndpoints,
    private val headerInstruction: CacheInstruction?,
    private val cacheStatisticsComposerFactory: CacheStatisticsComposer.Factory,
    private val isCacheEnabled: Boolean,
    private val filterFinal: Boolean
) : ObservableInteractor<MultiAddressResponse> {

    override fun call(): Observable<MultiAddressResponse> {
        return endpoints.getMultiAddress(
            context.getString(R.string.active_param),
            dejaVuHeader = headerInstruction
        ).filter { !filterFinal || !isCacheEnabled || it.metadata.cacheToken.status.isFinal }
            .compose(cacheStatisticsComposerFactory.create(this))
    }

    class Factory(
        private val context: Context,
        private val refreshInstruction: CacheInstruction,
        private val doNotCacheInstruction: CacheInstruction,
        private val endpoints: MultiAddressEndpoints,
        private val cacheStatisticsComposerFactory: CacheStatisticsComposer.Factory
    ) {
        fun create(
            isCacheEnabled: Boolean = true,
            forceRefresh: Boolean = false,
            filterFinal: Boolean = false
        ): GetMultiAddressInteractor {
            val headerInstruction = ifElse(
                isCacheEnabled,
                ifElse(forceRefresh, refreshInstruction, null),
                doNotCacheInstruction
            )

            return GetMultiAddressInteractor(
                context,
                endpoints,
                headerInstruction,
                cacheStatisticsComposerFactory,
                isCacheEnabled,
                filterFinal
            )
        }
    }

}

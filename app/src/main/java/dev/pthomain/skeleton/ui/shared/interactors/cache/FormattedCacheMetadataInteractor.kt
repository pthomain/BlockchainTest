package dev.pthomain.skeleton.ui.shared.interactors.cache

import android.content.Context
import dev.pthomain.skeleton.R
import dev.pthomain.skeleton.domain.interactors.cache.CacheMetadataInteractor
import io.reactivex.Observable
import dev.pthomain.android.boilerplate.core.interactors.ObservableInteractor
import dev.pthomain.android.boilerplate.core.utils.kotlin.ifElse
import dev.pthomain.android.boilerplate.core.utils.log.Logger
import dev.pthomain.android.boilerplate.core.utils.rx.observable
import dev.pthomain.android.dejavu.configuration.CacheInstruction.Operation.Type.*
import dev.pthomain.android.dejavu.interceptors.internal.cache.token.CacheStatus.*
import dev.pthomain.android.dejavu.interceptors.internal.error.Glitch
import dev.pthomain.android.dejavu.response.CacheMetadata
import java.text.SimpleDateFormat
import java.util.*

class FormattedCacheMetadataInteractor(
    private val context: Context,
    private val cacheMetadataInteractorFactory: CacheMetadataInteractor.Factory,
    private val logger: Logger
) : ObservableInteractor<String> {

    private val dateFormat = SimpleDateFormat("dd/MM/yy")
    private val timeFormat = SimpleDateFormat("hh:mm:ss")
    private val fullDateFormat = SimpleDateFormat("dd/MM/yy H:mm:ss")

    override fun call() =
        cacheMetadataInteractorFactory.create()
            .call()
            .flatMap { metadata ->
                if (metadata.exception == null)
                    getFormattedDescription(metadata).observable()
                else
                    getErrorDescription(metadata)
            }

    private fun getFormattedDescription(metadata: CacheMetadata<Glitch>): String {
        val cacheToken = metadata.cacheToken
        val status = cacheToken.status
        val fetchDate = cacheToken.fetchDate

        return when (status) {
            NOT_CACHED,
            NETWORK -> context.getString(R.string.fresh_data_metadata)

            REFRESHED -> context.getString(R.string.refreshed_data_metadata)

            FRESH,
            STALE,
            COULD_NOT_REFRESH -> {
                val formattedStatus = context.getString(
                    ifElse(
                        status.isFresh,
                        R.string.cache_status_fresh,
                        R.string.cache_status_stale
                    )
                )

                val formattedDate =
                    if (dateFormat.format(Date()) == dateFormat.format(fetchDate!!)) {
                        context.getString(
                            R.string.date_today_metadata,
                            timeFormat.format(fetchDate)
                        )
                    } else {
                        context.getString(
                            R.string.date_some_day_metadata,
                            fullDateFormat.format(fetchDate)
                        )
                    }

                context.getString(
                    R.string.cache_status,
                    formattedStatus,
                    formattedDate
                )
            }

            //remaining states should not be returned with the current cache configuration
            else -> throw IllegalStateException("Wrong state for cache metadata")
        }
    }

    private fun getErrorDescription(metadata: CacheMetadata<Glitch>): Observable<String> {
        val operation = metadata.cacheToken.instruction.operation.type
        val isCompletable = operation == CLEAR || operation == CLEAR_ALL || operation == INVALIDATE

        return with(metadata.exception!!) {
            if (isCompletable && cause.message == "This operation does not return any data: $operation") {
                //Completable operations on the cache may emit this, it can be safely ignored
                Observable.empty()
            } else {
                "Error $errorCode: ${cause.message}".also {
                    logger.e(
                        this@FormattedCacheMetadataInteractor,
                        this,
                        message
                    )
                }.observable()
            }
        }
    }

}

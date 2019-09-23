package dev.pthomain.skeleton.domain.cache

import io.reactivex.*
import uk.co.glass_software.android.boilerplate.core.utils.log.Logger
import uk.co.glass_software.android.boilerplate.core.utils.rx.On
import uk.co.glass_software.android.boilerplate.core.utils.rx.schedule
import uk.co.glass_software.android.dejavu.DejaVu


class CacheStatisticsComposer<T> private constructor(
    private val dejaVu: DejaVu<*>,
    private val logger: Logger,
    private val caller: Any
) : ObservableTransformer<T, T>,
    SingleTransformer<T, T>,
    CompletableTransformer {

    override fun apply(upstream: Observable<T>) =
        upstream.doFinally {
            printCacheStatistics()
        }

    override fun apply(upstream: Single<T>) =
        upstream.doFinally {
            printCacheStatistics()
        }

    override fun apply(upstream: Completable) =
        upstream.doFinally {
            printCacheStatistics()
        }

    private fun printCacheStatistics() =
        dejaVu.statistics()
            .schedule(On.Io)
            .subscribe(
                { it.log(logger) },
                { logger.e(caller, it) }
            )

    class Factory(
        private val dejaVu: DejaVu<*>,
        private val logger: Logger
    ) {
        fun <T> create(caller: Any) =
            CacheStatisticsComposer<T>(
                dejaVu,
                logger,
                caller
            )
    }

}

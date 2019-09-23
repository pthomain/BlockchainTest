package dev.pthomain.skeleton.ui.features.main

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.pthomain.skeleton.R
import dev.pthomain.skeleton.domain.interactors.cache.ClearCacheInteractor
import dev.pthomain.skeleton.domain.interactors.multi_address.GetMultiAddressInteractor
import dev.pthomain.skeleton.ui.shared.interactors.cache.FormattedCacheMetadataInteractor
import dev.pthomain.skeleton.ui.features.main.adapter.MultiAddressUiModel
import io.reactivex.disposables.CompositeDisposable
import dev.pthomain.android.boilerplate.core.mvvm.BaseViewModel
import dev.pthomain.android.boilerplate.core.rx.SimpleSuccessFailureComposerFactory
import dev.pthomain.android.boilerplate.core.rx.SuccessFailureComposerFactory
import dev.pthomain.android.boilerplate.core.utils.delegates.SharedPrefsDelegate
import dev.pthomain.android.boilerplate.core.utils.functional.ResultOrError.ResultOrThrowable
import dev.pthomain.android.boilerplate.core.utils.kotlin.ifElse
import dev.pthomain.android.boilerplate.core.utils.log.Logger
import dev.pthomain.android.boilerplate.core.utils.rx.ioUi
import dev.pthomain.android.dejavu.DejaVu
import dev.pthomain.android.dejavu.interceptors.internal.error.Glitch

typealias MultiAddressResult = ResultOrThrowable<List<MultiAddressUiModel>, *>
typealias MetaDataResult = ResultOrThrowable<String, *>
typealias HasDataResult = ResultOrThrowable<Boolean, *>

class MainViewModel(
    private val context: Context,
    logger: Logger,
    cacheMetadataInteractor: FormattedCacheMetadataInteractor,
    private val dejaVu: DejaVu<Glitch>,
    private val disposables: CompositeDisposable,
    isCacheEnabledDelegate: SharedPrefsDelegate<Boolean>,
    showTempStaleDataDelegate: SharedPrefsDelegate<Boolean>,
    private val mutableMultiAddressLiveData: MutableLiveData<MultiAddressResult>,
    private val mutableCachedMetadataLiveData: MutableLiveData<MetaDataResult>,
    private val mutableHasCachedEntriesLiveData: MutableLiveData<HasDataResult>,
    private val clearCacheInteractor: ClearCacheInteractor,
    private val getMultiAddressInteractorFactory: GetMultiAddressInteractor.Factory,
    composerFactory: SuccessFailureComposerFactory = SimpleSuccessFailureComposerFactory(),
    private val uiHandler: Handler = Handler(Looper.getMainLooper())
) : BaseViewModel(logger, disposables, composerFactory) {

    private var isCacheEnabledPref by isCacheEnabledDelegate
    private var showTempStaleDataPref by showTempStaleDataDelegate

    var isCacheEnabled: Boolean = isCacheEnabledPref ?: true
        set(value) {
            if (!value) clearCache()
            isCacheEnabledPref = value
            field = value
        }

    var showStaleTempData: Boolean = showTempStaleDataPref ?: true
        set(value) {
            showTempStaleDataPref = value
            field = value
        }

    val multiAddressLiveData = mutableMultiAddressLiveData as LiveData<MultiAddressResult>
    val cachedMetadataLiveData = mutableCachedMetadataLiveData as LiveData<MetaDataResult>
    val hasCachedEntriesLiveData = mutableHasCachedEntriesLiveData as LiveData<HasDataResult>

    init {
        cacheMetadataInteractor.call()
            .doFinally { updateStatistics() }
            .ioUi()
            .autoSubscribe(mutableCachedMetadataLiveData)

        updateStatistics()
    }

    fun loadMultiAddress(forceRefresh: Boolean = false) {
        updateCacheMetadata(
            context.getString(
                ifElse(
                    forceRefresh,
                    R.string.refreshing,
                    R.string.loading
                )
            )
        )

        getMultiAddressObservable(forceRefresh)
            .doOnNext { updateStatistics() }
            .map {
                val isTemporary = isCacheEnabled && !it.metadata.cacheToken.status.isFinal
                val isStale = isCacheEnabled && !it.metadata.cacheToken.status.isFresh
                val cacheDate = if (isStale) it.metadata.cacheToken.cacheDate else null

                it.txs.map { txs ->
                    MultiAddressUiModel(
                        it,
                        txs,
                        isTemporary,
                        cacheDate
                    )
                }.sortedByDescending { it.txs.time }
            }
            .ioUi()
            .autoSubscribe(mutableMultiAddressLiveData)
    }

    private fun updateStatistics() {
        dejaVu.statistics()
            .map { it.entries.isNotEmpty() }
            .ioUi()
            .autoSubscribe(mutableHasCachedEntriesLiveData)
    }

    private fun updateCacheMetadata(message: String) {
        uiHandler.post {
            mutableCachedMetadataLiveData.value = ResultOrThrowable.Result(message)
        }
    }

    private fun clearCache() {
        clearCacheInteractor.call()
            .doFinally { updateStatistics() }
            .ioUi()
            .subscribe(
                {
                    updateCacheMetadata(context.getString(R.string.cache_cleared))
                },
                {
                    val message = context.getString(R.string.cache_clearing_error)
                    updateCacheMetadata(message)
                    logger.e(this, it, message)
                }
            ).also { disposables.add(it) }
    }

    private fun getMultiAddressObservable(forceRefresh: Boolean) =
        getMultiAddressInteractorFactory.create(
            isCacheEnabled = isCacheEnabled,
            forceRefresh = forceRefresh,
            filterFinal = !showStaleTempData
        ).call()

}

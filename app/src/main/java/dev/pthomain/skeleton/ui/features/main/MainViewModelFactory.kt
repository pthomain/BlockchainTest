package dev.pthomain.skeleton.ui.features.main

import android.content.Context
import androidx.lifecycle.MutableLiveData
import dev.pthomain.skeleton.domain.interactors.cache.ClearCacheInteractor
import dev.pthomain.skeleton.domain.interactors.multi_address.GetMultiAddressInteractor
import dev.pthomain.skeleton.ui.shared.interactors.cache.FormattedCacheMetadataInteractor
import io.reactivex.disposables.CompositeDisposable
import uk.co.glass_software.android.boilerplate.core.utils.BaseViewModelFactory
import uk.co.glass_software.android.boilerplate.core.utils.delegates.SharedPrefsDelegate
import uk.co.glass_software.android.boilerplate.core.utils.log.Logger
import uk.co.glass_software.android.dejavu.DejaVu
import uk.co.glass_software.android.dejavu.interceptors.internal.error.Glitch

class MainViewModelFactory(
    private val context: Context,
    private val logger: Logger,
    private val dejaVu: DejaVu<Glitch>,
    private val formattedCacheMetadataInteractor: FormattedCacheMetadataInteractor,
    private val compositeDisposable: CompositeDisposable,
    private val isCacheEnabledDelegate: SharedPrefsDelegate<Boolean>,
    private val showTempStaleDataDelegate: SharedPrefsDelegate<Boolean>,
    private val mutableMultiAddressLiveData: MutableLiveData<MultiAddressResult>,
    private val mutableCachedMetadataLiveData: MutableLiveData<MetaDataResult>,
    private val mutableHasCachedEntriesLiveData: MutableLiveData<HasDataResult>,
    private val clearCacheInteractor: ClearCacheInteractor,
    private val getMultiAddressInteractorFactory: GetMultiAddressInteractor.Factory
) : BaseViewModelFactory<MainViewModel>(MainViewModel::class.java) {

    override fun create() = MainViewModel(
        context,
        logger,
        formattedCacheMetadataInteractor,
        dejaVu,
        compositeDisposable,
        isCacheEnabledDelegate,
        showTempStaleDataDelegate,
        mutableMultiAddressLiveData,
        mutableCachedMetadataLiveData,
        mutableHasCachedEntriesLiveData,
        clearCacheInteractor,
        getMultiAddressInteractorFactory
    )

}


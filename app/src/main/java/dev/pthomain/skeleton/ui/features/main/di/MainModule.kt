package dev.pthomain.skeleton.ui.features.main.di

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import dagger.Module
import dagger.Provides
import dev.pthomain.skeleton.domain.interactors.cache.ClearCacheInteractor
import dev.pthomain.skeleton.domain.interactors.multi_address.GetMultiAddressInteractor
import dev.pthomain.skeleton.ui.di.FragmentScope
import dev.pthomain.skeleton.ui.features.main.*
import dev.pthomain.skeleton.ui.features.main.adapter.MultiAddressAdapter
import dev.pthomain.skeleton.ui.shared.interactors.cache.FormattedCacheMetadataInteractor
import dev.pthomain.skeleton.ui.shared.interactors.di.SharedModule
import io.reactivex.disposables.CompositeDisposable
import uk.co.glass_software.android.boilerplate.core.utils.delegates.Prefs
import uk.co.glass_software.android.boilerplate.core.utils.delegates.SharedPrefsDelegate
import uk.co.glass_software.android.boilerplate.core.utils.log.Logger
import uk.co.glass_software.android.dejavu.DejaVu
import uk.co.glass_software.android.dejavu.interceptors.internal.error.Glitch
import javax.inject.Named

@Module(includes = [SharedModule::class])
class MainModule(
    private val context: Context,
    private val mainFragment: MainFragment
) {

    @Provides
    @FragmentScope
    fun provideMainViewModelFactory(
        logger: Logger,
        dejaVu: DejaVu<Glitch>,
        formattedCacheMetadataInteractor: FormattedCacheMetadataInteractor,
        compositeDisposable: CompositeDisposable,
        @Named(IS_CACHE_ENABLED) isCacheEnabledDelegate: SharedPrefsDelegate<Boolean>,
        @Named(SHOW_STALE_TEMP_DATA) showTempStaleDataDelegate: SharedPrefsDelegate<Boolean>,
        mutableMultiAddressLiveData: MutableLiveData<MultiAddressResult>,
        mutableCachedMetadataLiveData: MutableLiveData<MetaDataResult>,
        mutableHasCachedEntriesLiveData: MutableLiveData<HasDataResult>,
        clearCacheInteractor: ClearCacheInteractor,
        getMultiAddressInteractorFactory: GetMultiAddressInteractor.Factory
    ) =
        MainViewModelFactory(
            context,
            logger,
            dejaVu,
            formattedCacheMetadataInteractor,
            compositeDisposable,
            isCacheEnabledDelegate,
            showTempStaleDataDelegate,
            mutableMultiAddressLiveData,
            mutableCachedMetadataLiveData,
            mutableHasCachedEntriesLiveData,
            clearCacheInteractor,
            getMultiAddressInteractorFactory
        )

    @Provides
    @FragmentScope
    @Named(IS_CACHE_ENABLED)
    //personal lib, see https://github.com/pthomain/boilerplate
    fun provideIsCacheEnabledPrefDelegate(prefs: Prefs) =
        SharedPrefsDelegate<Boolean>(prefs, IS_CACHE_ENABLED.toLowerCase())

    @Provides
    @FragmentScope
    @Named(SHOW_STALE_TEMP_DATA)
    //personal lib, see https://github.com/pthomain/boilerplate
    fun provideShowTempStaleDataPrefDelegate(prefs: Prefs) =
        SharedPrefsDelegate<Boolean>(prefs, SHOW_STALE_TEMP_DATA.toLowerCase())

    @Provides
    @FragmentScope
    fun provideMutableMultiAddressResultLiveData() =
        MutableLiveData<MultiAddressResult>()

    @Provides
    @FragmentScope
    fun provideMutableCachedMetadataLiveData() =
        MutableLiveData<MetaDataResult>()

    @Provides
    @FragmentScope
    fun provideMutableHasCachedEntriesLiveData() =
        MutableLiveData<HasDataResult>()

    @Provides
    @FragmentScope
    fun provideMainViewModel(mainViewModelFactory: MainViewModelFactory) =
        ViewModelProviders.of(
            mainFragment,
            mainViewModelFactory
        ).get(MainViewModel::class.java)

    @Provides
    @FragmentScope
    fun provideCompositeDisposable() =
        CompositeDisposable()

    @Provides
    @FragmentScope
    fun provideMultiAddressAdapter() =
        MultiAddressAdapter()

    @Provides
    @FragmentScope
    fun provideLinearLayoutManager() =
        LinearLayoutManager(context)

    companion object {
        const val IS_CACHE_ENABLED = "IS_CACHE_ENABLED"
        const val SHOW_STALE_TEMP_DATA = "SHOW_STALE_TEMP_DATA"
    }

}

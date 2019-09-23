package dev.pthomain.skeleton.ui.features.main

import android.content.Context
import android.os.Handler
import androidx.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.*
import dev.pthomain.skeleton.R
import dev.pthomain.skeleton.base.view_model.prepareInteractorCallMocking
import dev.pthomain.skeleton.base.view_model.verifyInteractorCall
import dev.pthomain.skeleton.domain.api.multi_address.models.MultiAddressResponse
import dev.pthomain.skeleton.domain.interactors.cache.ClearCacheInteractor
import dev.pthomain.skeleton.domain.interactors.multi_address.GetMultiAddressInteractor
import dev.pthomain.skeleton.ui.shared.interactors.cache.FormattedCacheMetadataInteractor
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.CompositeDisposable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import dev.pthomain.android.boilerplate.core.rx.SuccessFailureComposerFactory
import dev.pthomain.android.boilerplate.core.utils.delegates.SharedPrefsDelegate
import dev.pthomain.android.boilerplate.core.utils.functional.ResultOrError.ResultOrThrowable.Result
import dev.pthomain.android.boilerplate.core.utils.functional.SuccessFailure
import dev.pthomain.android.boilerplate.core.utils.log.Logger
import dev.pthomain.android.boilerplate.core.utils.rx.On
import dev.pthomain.android.boilerplate.core.utils.rx.RxTransformer
import dev.pthomain.android.dejavu.DejaVu
import dev.pthomain.android.dejavu.interceptors.internal.error.Glitch

class MainViewModelUnitTest {

    private val mockContext: Context = mock()
    private val mockLogger: Logger = mock()
    private val mockCacheMetadataInteractor: FormattedCacheMetadataInteractor = mock()
    private val mockDejaVu: DejaVu<Glitch> = mock()
    private val mockDisposables: CompositeDisposable = mock()
    private val mockIsCacheEnabledDelegate: SharedPrefsDelegate<Boolean> = mock()
    private val mockShowTempStaleDataDelegate: SharedPrefsDelegate<Boolean> = mock()
    private val mockMutableMultiAddressLiveData: MutableLiveData<MultiAddressResult> = mock()
    private val mockMutableCachedMetadataLiveData: MutableLiveData<MetaDataResult> = mock()
    private val mockMutableHasCachedEntriesLiveData: MutableLiveData<HasDataResult> = mock()
    private val mockClearCacheInteractor: ClearCacheInteractor = mock()
    private val mockGetMultiAddressInteractorFactory: GetMultiAddressInteractor.Factory = mock()
    private val mockComposerFactory: SuccessFailureComposerFactory = mock()
    private val mockUiHandler: Handler = mock()

    private lateinit var target: MainViewModel

    private inline fun <reified T> resultOrErrorTransformer(mockObservable: Observable<SuccessFailure<T>>) =
        object : RxTransformer<T, SuccessFailure<T>>() {
            override fun apply(upstream: Observable<T>) = mockObservable
        }

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { On.Trampoline() }
        resetMocks()

        whenever(mockDejaVu.statistics()).thenReturn(Single.just(mock()))

        val preparedCacheMetadataObservable =
            prepareInteractorCallMocking(
                mockCacheMetadataInteractor,
                "mockValue"
            )

        whenever(mockComposerFactory.newComposer<String>(any(), any()))
            .thenReturn(resultOrErrorTransformer(preparedCacheMetadataObservable))

        target = MainViewModel(
            mockContext,
            mockLogger,
            mockCacheMetadataInteractor,
            mockDejaVu,
            mockDisposables,
            mockIsCacheEnabledDelegate,
            mockShowTempStaleDataDelegate,
            mockMutableMultiAddressLiveData,
            mockMutableCachedMetadataLiveData,
            mockMutableHasCachedEntriesLiveData,
            mockClearCacheInteractor,
            mockGetMultiAddressInteractorFactory,
            mockComposerFactory,
            mockUiHandler
        )

        verifyInteractorCall(preparedCacheMetadataObservable)
    }

    private fun resetMocks() {
        reset(
            mockContext,
            mockLogger,
            mockCacheMetadataInteractor,
            mockDejaVu,
            mockDisposables,
            mockIsCacheEnabledDelegate,
            mockShowTempStaleDataDelegate,
            mockMutableMultiAddressLiveData,
            mockMutableCachedMetadataLiveData,
            mockMutableHasCachedEntriesLiveData,
            mockClearCacheInteractor,
            mockGetMultiAddressInteractorFactory
        )
    }

    @Test
    fun testLoadMultiAddress() {
        val loading = "loading"

        whenever(mockContext.getString(eq(R.string.loading)))
            .thenReturn(loading)

        val mockGetMultiAddressInteractor = mock<GetMultiAddressInteractor>()
        whenever(
            mockGetMultiAddressInteractorFactory.create(
                eq(true),
                eq(false),
                eq(false)
            )
        ).thenReturn(mockGetMultiAddressInteractor)

        val mockMultiAddressResponse = mock<MultiAddressResponse>()
        val mockMultiAddressObservable = Observable.just(mockMultiAddressResponse)
        whenever(mockGetMultiAddressInteractor.call()).thenReturn(mockMultiAddressObservable)

        target.loadMultiAddress()

        val runnableCaptor = argumentCaptor<Runnable>()
        verify(mockUiHandler).post(runnableCaptor.capture())

        runnableCaptor.firstValue.run()

        val metadataMessageCaptor = argumentCaptor<Result<String>>()
        verify(mockMutableCachedMetadataLiveData).setValue(metadataMessageCaptor.capture())

        assertEquals(
            "The cache metadata message didn't match",
            loading,
            metadataMessageCaptor.firstValue.value
        )
    }
}
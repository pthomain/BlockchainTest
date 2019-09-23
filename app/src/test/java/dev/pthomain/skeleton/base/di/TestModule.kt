package dev.pthomain.skeleton.base.di

import android.content.Context
import com.google.gson.Gson
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.spy
import dagger.Module
import dagger.Provides
import dev.pthomain.skeleton.R
import dev.pthomain.skeleton.base.utils.AssetHelper
import dev.pthomain.skeleton.base.utils.MockClient
import dev.pthomain.skeleton.domain.api.cache.CacheEndpoints
import dev.pthomain.skeleton.domain.api.cache.NoOpEncryptionManager
import dev.pthomain.skeleton.domain.api.multi_address.MultiAddressEndpoints
import dev.pthomain.skeleton.domain.cache.CacheStatisticsComposer
import dev.pthomain.skeleton.domain.serialisation.di.SerialisationModule
import dev.pthomain.skeleton.domain.serialisation.gson.GsonSerialiser
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.co.glass_software.android.boilerplate.core.utils.log.Logger
import uk.co.glass_software.android.dejavu.DejaVu
import uk.co.glass_software.android.dejavu.interceptors.internal.error.Glitch
import javax.inject.Singleton

@Module(includes = [SerialisationModule::class])
class TestModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideAssetHelper(gson: Gson) =
        AssetHelper(
            ASSETS_FOLDER,
            gson
        )

    @Provides
    @Singleton
    fun provideMockClient() =
        spy(MockClient())

    @Provides
    @Singleton
    fun provideCacheEndpoints(retrofit: Retrofit) =
        spy(retrofit.create(CacheEndpoints::class.java))

    @Provides
    @Singleton
    fun provideMultiAddressEndpoints(retrofit: Retrofit) =
        spy(retrofit.create(MultiAddressEndpoints::class.java))

    @Provides
    @Singleton
    fun provideCachedCallAdapterFactory(dejaVu: DejaVu<Glitch>): CallAdapter.Factory =
        spy(dejaVu.retrofitCallAdapterFactory)

    @Provides
    @Singleton
    fun provideCachedRetrofit(
        context: Context,
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        callAdapterFactory: CallAdapter.Factory
    ) =
        Retrofit.Builder()
            .client(okHttpClient)
            .addCallAdapterFactory(callAdapterFactory)
            .addConverterFactory(gsonConverterFactory)
            .baseUrl(context.getString(R.string.base_api_url))
            .build()

    @Provides
    @Singleton
    fun provideDejaVu(
        context: Context,
        logger: Logger,
        gsonSerialiser: GsonSerialiser
    ) =
        //personal lib for caching, see https://github.com/pthomain/dejavu
        DejaVu.builder()
            .logger(logger)
            .encryption { NoOpEncryptionManager() }
            .useFileCaching()
            .encryptByDefault(false)
            .compressByDefault(false)
            .build(context, gsonSerialiser)

    @Provides
    @Singleton
    fun provideCacheStatisticsComposerFactory(
        dejaVu: DejaVu<Glitch>,
        logger: Logger
    ) =
        CacheStatisticsComposer.Factory(
            dejaVu,
            logger
        )

    @Provides
    @Singleton
    fun provideOkHttp(mockClient: MockClient) =
        OkHttpClient.Builder().apply {
            addInterceptor(mockClient)
        }.build()

    companion object {
        const val ASSETS_FOLDER = "src/main/assets/"
    }

}


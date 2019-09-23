package dev.pthomain.skeleton.domain.network.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dev.pthomain.skeleton.R
import dev.pthomain.skeleton.domain.serialisation.di.SerialisationModule
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.co.glass_software.android.dejavu.DejaVu
import uk.co.glass_software.android.dejavu.interceptors.internal.error.Glitch
import javax.inject.Singleton

@Module(includes = [SerialisationModule::class])
class NetworkModule(
    private val okHttpClient: OkHttpClient? = null
) {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor() =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor) =
        okHttpClient ?: OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideCachedCallAdapterFactory(dejaVu: DejaVu<Glitch>): CallAdapter.Factory =
        //personal lib for caching, see https://github.com/pthomain/dejavu
        dejaVu.retrofitCallAdapterFactory

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

}
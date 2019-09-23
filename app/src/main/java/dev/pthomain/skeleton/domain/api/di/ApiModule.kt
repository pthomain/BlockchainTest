package dev.pthomain.skeleton.domain.api.di

import dagger.Module
import dagger.Provides
import dev.pthomain.skeleton.domain.api.cache.CacheEndpoints
import dev.pthomain.skeleton.domain.api.multi_address.MultiAddressEndpoints
import dev.pthomain.skeleton.domain.network.di.NetworkModule
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
class ApiModule {

    @Provides
    @Singleton
    fun provideCacheEndpoints(retrofit: Retrofit) =
        retrofit.create(CacheEndpoints::class.java)

    @Provides
    @Singleton
    fun provideMultiAddressEndpoints(retrofit: Retrofit) =
        retrofit.create(MultiAddressEndpoints::class.java)

}

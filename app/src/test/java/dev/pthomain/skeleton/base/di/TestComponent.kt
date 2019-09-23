package dev.pthomain.skeleton.base.di

import dagger.Component
import dev.pthomain.skeleton.base.utils.AssetHelper
import dev.pthomain.skeleton.base.utils.MockClient
import dev.pthomain.skeleton.di.AppComponent
import dev.pthomain.skeleton.domain.api.cache.CacheEndpoints
import dev.pthomain.skeleton.domain.api.multi_address.MultiAddressEndpoints
import dev.pthomain.skeleton.domain.interactors.di.InteractorModule
import okhttp3.OkHttpClient
import dev.pthomain.android.boilerplate.core.utils.log.Logger
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        InteractorModule::class,
        TestModule::class
    ]
)
interface TestComponent : AppComponent {

    fun okHttpClient(): OkHttpClient
    fun mockClient(): MockClient
    fun assetHelper(): AssetHelper

    fun cacheEndpoints(): CacheEndpoints
    fun multiAddressEndpoints(): MultiAddressEndpoints

}
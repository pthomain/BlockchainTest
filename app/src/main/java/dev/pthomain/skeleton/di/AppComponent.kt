package dev.pthomain.skeleton.di

import dagger.Component
import dev.pthomain.skeleton.domain.api.cache.CacheModule
import dev.pthomain.skeleton.domain.api.di.ApiModule
import dev.pthomain.skeleton.domain.interactors.cache.CacheMetadataInteractor
import dev.pthomain.skeleton.domain.interactors.cache.ClearCacheInteractor
import dev.pthomain.skeleton.domain.interactors.di.InteractorModule
import dev.pthomain.skeleton.domain.interactors.multi_address.GetMultiAddressInteractor
import dev.pthomain.android.boilerplate.core.utils.delegates.Prefs
import dev.pthomain.android.boilerplate.core.utils.log.Logger
import dev.pthomain.android.dejavu.DejaVu
import dev.pthomain.android.dejavu.interceptors.internal.error.Glitch
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        InteractorModule::class,
        CacheModule::class,
        ApiModule::class
    ]
)
interface AppComponent {

    //personal lib, see https://github.com/pthomain/boilerplate
    fun logger(): Logger

    fun prefs(): Prefs

    //personal lib, see https://github.com/pthomain/dejavu
    fun dejaVu(): DejaVu<Glitch>

    fun cacheMetadataInteractorFactory(): CacheMetadataInteractor.Factory
    fun clearCacheInteractor(): ClearCacheInteractor
    fun getMultiAddressInteractorFactory(): GetMultiAddressInteractor.Factory

}

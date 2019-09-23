package dev.pthomain.skeleton.ui.shared.interactors.di

import android.content.Context
import com.bumptech.glide.Glide
import dagger.Module
import dagger.Provides
import dev.pthomain.skeleton.domain.interactors.cache.CacheMetadataInteractor
import dev.pthomain.skeleton.ui.di.FragmentScope
import dev.pthomain.skeleton.ui.shared.interactors.cache.FormattedCacheMetadataInteractor
import uk.co.glass_software.android.boilerplate.core.utils.log.Logger

@Module
class SharedModule(private val context: Context) {

    @Provides
    @FragmentScope
    fun formattedCacheMetadataInteractor(
        cacheMetadataInteractorFactory: CacheMetadataInteractor.Factory,
        logger: Logger
    ) =
        FormattedCacheMetadataInteractor(
            context,
            cacheMetadataInteractorFactory,
            logger
        )

    @Provides
    @FragmentScope
    fun provideRequestManager() = Glide.with(context)
}

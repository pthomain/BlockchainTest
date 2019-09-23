package dev.pthomain.skeleton.domain.serialisation.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dev.pthomain.skeleton.domain.di.DomainModule
import dev.pthomain.skeleton.domain.serialisation.gson.GsonSerialiser
import retrofit2.converter.gson.GsonConverterFactory
import uk.co.glass_software.android.boilerplate.core.utils.delegates.Prefs
import uk.co.glass_software.android.boilerplate.core.utils.delegates.Prefs.Companion.prefs
import javax.inject.Singleton

@Module(includes = [DomainModule::class])
class SerialisationModule {

    @Provides
    @Singleton
    fun provideGson() =
        GsonBuilder().create()

    @Provides
    @Singleton
    fun provideGsonSerialiser(gson: Gson) =
        GsonSerialiser(gson)

    @Provides
    @Singleton
    fun provideGsonConverterFactory() =
        GsonConverterFactory.create()

    @Provides
    @Singleton
    fun providePrefs(context: Context): Prefs =
        context.prefs(PREFS_FILE)

    companion object {
        const val PREFS_FILE = "skeleton_prefs"
    }

}

package dev.pthomain.skeleton.domain.api.cache

import io.reactivex.Completable
import retrofit2.http.DELETE
import uk.co.glass_software.android.dejavu.retrofit.annotations.ClearAll

interface CacheEndpoints {

    @DELETE("/")
    @ClearAll
    fun clearCache(): Completable

}
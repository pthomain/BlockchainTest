package dev.pthomain.skeleton.domain.api.multi_address

import dev.pthomain.skeleton.domain.api.multi_address.models.MultiAddressResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import dev.pthomain.android.dejavu.DejaVu.Companion.DejaVuHeader
import dev.pthomain.android.dejavu.configuration.CacheInstruction
import dev.pthomain.android.dejavu.retrofit.annotations.Cache

interface MultiAddressEndpoints {

    @GET("multiaddr")
    @Cache(durationInMillis = CACHE_DURATION_MILLIS) //Provides default Cache instruction (if dejaVuHeader is null)
    fun getMultiAddress(
        @Query("active") active: String,
        @Header(DejaVuHeader) dejaVuHeader: CacheInstruction? = null //Overrides the annotation's instruction if set (i.e for Refresh/DoNotCache)
    ): Observable<MultiAddressResponse>

    companion object {
        const val CACHE_DURATION_MILLIS = 10000L //10s
    }
}

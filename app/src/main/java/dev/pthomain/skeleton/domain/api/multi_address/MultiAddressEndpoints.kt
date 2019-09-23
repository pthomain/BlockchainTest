package dev.pthomain.skeleton.domain.api.multi_address

import dev.pthomain.skeleton.domain.api.multi_address.models.MultiAddressResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import uk.co.glass_software.android.dejavu.DejaVu.Companion.DejaVuHeader
import uk.co.glass_software.android.dejavu.configuration.CacheInstruction
import uk.co.glass_software.android.dejavu.retrofit.annotations.Cache

interface MultiAddressEndpoints {

    @GET("multiaddr")
    @Cache(durationInMillis = CACHE_DURATION_MILLIS)
    fun getMultiAddress(
        @Query("active") active: String,
        @Header(DejaVuHeader) dejaVuHeader: CacheInstruction? = null
    ): Observable<MultiAddressResponse>

    companion object {
        const val CACHE_DURATION_MILLIS = 10000L //10s
    }
}

package dev.pthomain.skeleton.base.utils

import com.google.gson.Gson
import io.reactivex.Observable
import dev.pthomain.android.boilerplate.core.utils.io.useAndLogError
import dev.pthomain.android.dejavu.configuration.NetworkErrorProvider
import dev.pthomain.android.dejavu.response.CacheMetadata
import java.io.*

class AssetHelper(private val assetsFolder: String,
                  private val gson: Gson) {

    fun <E, R> observeStubbedResponse(fileName: String,
                                      responseClass: Class<R>)
            : Observable<R> where E : Exception,
                                  E : NetworkErrorProvider,
                                  R : CacheMetadata.Holder<E> =
        observeFile(fileName)
            .map { gson.fromJson(it, responseClass) }

    fun observeFile(fileName: String): Observable<String> =
        File(assetsFolder + fileName).let {
            FileInputStream(it).useAndLogError({
                Observable.just(fileToString(it))
            })
        }

    @Throws(IOException::class)
    private fun fileToString(inputStream: InputStream) =
        BufferedReader(InputStreamReader(inputStream, "UTF-8")).useAndLogError({ reader ->
            val builder = StringBuilder()
            var line: String?
            do {
                line = reader.readLine()
                if (line != null) {
                    builder.append(line)
                    builder.append('\n')
                }
            } while (line != null)
            builder.toString()
        })

}

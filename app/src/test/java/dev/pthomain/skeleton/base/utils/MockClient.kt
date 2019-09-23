package dev.pthomain.skeleton.base.utils

import junit.framework.Assert.fail
import okhttp3.Interceptor
import okhttp3.Protocol.HTTP_1_1
import okhttp3.Request
import okhttp3.Response
import okhttp3.internal.http.RealResponseBody
import okio.Buffer
import java.io.IOException
import java.util.*

/**
 * This class provides a way to enqueue HTTP responses for mocking purposes in integration tests.
 * It works in conjunction with the addition of an interceptor to the OkHttpClient.
 *
 *
 * See https://github.com/square/okhttp/issues/1096
 */
class MockClient : Interceptor {

    private val events = ArrayDeque<Any>()
    private val requests = ArrayDeque<Request>()

    val requestHistory: List<Request>
        get() = LinkedList(requests)

    /**
     * Does the interception of the request and bypasses the network call by returning the mocked
     * responses or exception enqueued prior to the network call being made.
     * Refer to OkHttp documentation for more information.
     */
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response? {
        val request = chain.request()
        requests.addLast(request)

        val event: Any

        try {
            event = events.removeFirst()
        } catch (nse: NoSuchElementException) {
            fail("No request in queue")
            return null
        }

        if (event is IOException) {
            throw event
        }
        if (event is RuntimeException) {
            throw event
        }
        if (event is ResponseWrapper) {
            val responseWrapper = event
            return responseWrapper.responseBuilder
                    .request(request)
                    .protocol(HTTP_1_1)
                    .code(responseWrapper.httpCode)
                    .build()
        }
        throw IllegalStateException("Unknown event " + event.javaClass)
    }

    /**
     * Enqueues a new HTTP response to be delivered in order during the test.
     *
     * @param responseWrapper the response wrapper to be enqueued
     */
    fun enqueueResponse(responseWrapper: ResponseWrapper) {
        events.addLast(responseWrapper)
    }

    /**
     * Delegates call to MockClient for convenience
     *
     * @param response the mocked HTTP response uniqueParameters
     * @param httpCode the mocked HTTP response status code
     * @see MockClient.enqueueResponse
     */
    fun enqueueResponse(response: String,
                        httpCode: Int) {
        val responseBuilder = Response.Builder()
        val source = Buffer()
        source.write(response.toByteArray())
        val body = RealResponseBody("text/plain", response.length.toLong(), source)
        responseBuilder.body(body)
        enqueueResponse(
            ResponseWrapper(
                responseBuilder,
                httpCode
            )
        )
    }

    /**
     * Enqueues a RuntimeException to be thrown in order during the test.
     *
     * @param exception the exception to be thrown
     */
    fun enqueueRuntimeException(exception: RuntimeException) {
        events.addLast(exception)
    }

    /**
     * Enqueues a IOException to be thrown in order during the test.
     *
     * @param exception the exception to be thrown
     */
    fun enqueueIOException(exception: IOException) {
        events.addLast(exception)
    }

    fun clearRequestHistory() {
        requests.clear()
    }

    /**
     * Class used to wrap the Response.Builder along with the mocked HTTP status code, since this
     * is not available on the builder itself.
     */
    class ResponseWrapper(val responseBuilder: Response.Builder,
                          val httpCode: Int)
}
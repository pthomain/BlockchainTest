package dev.pthomain.skeleton.base.integration.components.test

import dev.pthomain.skeleton.base.di.TestComponent
import dev.pthomain.skeleton.base.integration.BaseIntegrationTest
import dev.pthomain.skeleton.base.utils.AssetHelper
import dev.pthomain.skeleton.base.utils.MockClient
import dev.pthomain.skeleton.domain.api.multi_address.models.MultiAddressResponse
import okhttp3.OkHttpClient
import dev.pthomain.android.dejavu.DejaVu
import dev.pthomain.android.dejavu.interceptors.internal.error.Glitch
import java.io.IOException

abstract class BaseTestComponentIntegrationTest<T : Any>(
    targetExtractor: (TestComponent) -> T
) : BaseIntegrationTest<TestComponent, T>(
    TestComponentProvider(),
    targetExtractor
) {

    protected lateinit var dejavu: DejaVu<Glitch>
    protected lateinit var okHttpClient: OkHttpClient
    protected lateinit var mockClient: MockClient
    protected lateinit var assetHelper: AssetHelper

    protected fun enqueueResponse(
        response: String,
        httpCode: Int = 200
    ) {
        mockClient.enqueueResponse(response, httpCode)
    }

    protected fun enqueueRuntimeException(exception: RuntimeException) {
        mockClient.enqueueRuntimeException(exception)
    }

    protected fun enqueueIOException(exception: IOException) {
        mockClient.enqueueIOException(exception)
    }

    protected fun getStubbedMultiAddressResponseModel() =
        assetHelper.observeStubbedResponse(
            STUB_FILE,
            MultiAddressResponse::class.java
        ).blockingFirst()

    protected fun getStubbedMultiAddressResponseString() =
        assetHelper.observeFile(STUB_FILE)
            .blockingFirst()

    companion object {
        const val STUB_FILE = "api_stub_multi_address.json"
    }
}


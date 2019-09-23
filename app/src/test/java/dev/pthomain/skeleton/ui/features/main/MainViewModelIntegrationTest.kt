package dev.pthomain.skeleton.ui.features.main

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.verify
import dev.pthomain.skeleton.ui.features.main.base.BaseMainComponentIntegrationTest
import dev.pthomain.skeleton.ui.features.main.base.MainTestComponent
import org.junit.Assert.assertEquals
import org.junit.Test
import org.robolectric.annotation.LooperMode
import org.robolectric.annotation.LooperMode.Mode.PAUSED

@LooperMode(PAUSED)
class MainViewModelIntegrationTest
    : BaseMainComponentIntegrationTest<MainViewModel>(MainTestComponent::viewModel) {

    @Test
    fun `GIVEN that loadMultiAddress() is called with forceRefresh == false THEN the cache metadata is updated to "Loading"`() {
        val liveData = component.mutableCachedMetadataLiveData()

        target.loadMultiAddress(false)

        val argumentCaptor = argumentCaptor<MetaDataResult>()
        verify(liveData).setValue(argumentCaptor.capture())

        assertEquals(
            "The cache metadata status didn't match",
            "Loading",
            liveData.value
        )
    }

    @Test
    fun `GIVEN that loadMultiAddress() is called with forceRefresh == false THEN a call is made to the API AND the result is published to the LiveData`() {

    }

}
package dev.pthomain.skeleton.ui.features.main.base

import androidx.lifecycle.MutableLiveData
import dagger.Component
import dev.pthomain.skeleton.base.di.TestComponent
import dev.pthomain.skeleton.ui.di.FragmentScope
import dev.pthomain.skeleton.ui.features.main.MetaDataResult
import dev.pthomain.skeleton.ui.features.main.di.MainComponent

@Component(
    modules = [MainTestModule::class],
    dependencies = [TestComponent::class]
)
@FragmentScope
interface MainTestComponent : MainComponent {

    fun mutableCachedMetadataLiveData(): MutableLiveData<MetaDataResult>

}

package dev.pthomain.skeleton.ui.features.main.di

import androidx.recyclerview.widget.LinearLayoutManager
import dagger.Component
import dev.pthomain.skeleton.di.AppComponent
import dev.pthomain.skeleton.domain.interactors.multi_address.GetMultiAddressInteractor
import dev.pthomain.skeleton.ui.di.FragmentScope
import dev.pthomain.skeleton.ui.features.main.MainViewModel
import dev.pthomain.skeleton.ui.features.main.adapter.MultiAddressAdapter
import uk.co.glass_software.android.boilerplate.core.utils.log.Logger

@Component(
    modules = [MainModule::class],
    dependencies = [AppComponent::class]
)
@FragmentScope
interface MainComponent {

    fun logger(): Logger
    fun viewModel(): MainViewModel
    fun multiAddressAdapter(): MultiAddressAdapter
    fun layoutManager(): LinearLayoutManager
    fun getMultiAddressInteractorFactory(): GetMultiAddressInteractor.Factory

}
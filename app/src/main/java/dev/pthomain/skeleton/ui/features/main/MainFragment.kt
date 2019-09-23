package dev.pthomain.skeleton.ui.features.main

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dev.pthomain.android.boilerplate.core.mvvm.observeSuccessFailure
import dev.pthomain.android.boilerplate.core.utils.kotlin.ifElse
import dev.pthomain.skeleton.R
import dev.pthomain.skeleton.di.ComponentProvider
import dev.pthomain.skeleton.ui.di.BaseInjectedFragment
import dev.pthomain.skeleton.ui.features.main.adapter.MultiAddressUiModel
import dev.pthomain.skeleton.ui.features.main.di.MainComponent
import dev.pthomain.skeleton.ui.features.main.di.MainComponentProvider
import dev.pthomain.skeleton.ui.utils.getAmountColourInt
import dev.pthomain.skeleton.ui.utils.satoshiToBtc

class MainFragment(
    componentProvider: (MainFragment) -> ComponentProvider<out MainComponent> = { MainComponentProvider(it) }
) : BaseInjectedFragment<MainComponent, MainFragment>(componentProvider::invoke) {

    override fun layout() = R.layout.fragment_main

    private lateinit var cacheSwitch: Switch
    private lateinit var showStaleTempDataSwitch: Switch
    private lateinit var balanceLayout: LinearLayout
    private lateinit var balanceValue: TextView
    private lateinit var refreshLayout: SwipeRefreshLayout
    private lateinit var cacheMetadata: TextView
    private lateinit var refreshStatus: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: MainViewModel

    override fun onLayoutInflated(layout: View) {
        with(layout) {
            cacheSwitch = findViewById(R.id.cache_switch)
            showStaleTempDataSwitch = findViewById(R.id.show_stale_temp_data_switch)
            refreshLayout = findViewById(R.id.refresh_layout)
            balanceLayout = findViewById(R.id.balance_layout)
            balanceValue = findViewById(R.id.balance_value)
            cacheMetadata = findViewById(R.id.cache_metadata)
            refreshStatus = findViewById(R.id.refresh_status)
            recyclerView = findViewById(R.id.recycler_view)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val multiAddressAdapter = component.multiAddressAdapter()
        val logger = component.logger()

        recyclerView.adapter = component.multiAddressAdapter()
        recyclerView.layoutManager = component.layoutManager()

        viewModel = component.viewModel()

        viewModel.multiAddressLiveData.observeSuccessFailure(
            this,
            {
                multiAddressAdapter.update(it)
                updateAmount(it)
                refreshLayout.isRefreshing = false
            },
            {
                if (multiAddressAdapter.itemCount == 0) {
                    updateAmount(null)
                }
                logger.e(this, it)
                refreshLayout.isRefreshing = false
            }
        )

        cacheSwitch.isChecked = viewModel.isCacheEnabled
        showStaleTempDataSwitch.isChecked = viewModel.showStaleTempData

        cacheSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.isCacheEnabled = isChecked
        }

        showStaleTempDataSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.showStaleTempData = isChecked
        }

        viewModel.cachedMetadataLiveData.observeSuccessFailure(
            this,
            { cacheMetadata.text = it }
        )

        viewModel.hasCachedEntriesLiveData.observeSuccessFailure(
            this,
            {
                refreshStatus.text = getString(
                    ifElse(
                        it,
                        R.string.cache_entries_available,
                        R.string.cache_entries_unavailable
                    )
                )
            }
        )

        refreshLayout.setOnRefreshListener {
            viewModel.loadMultiAddress(true)
        }
    }

    private fun updateAmount(transactionList: List<MultiAddressUiModel>?) {
        transactionList
            ?.firstOrNull()
            ?.multiAddress
            ?.wallet
            ?.final_balance
            .also { balance ->
                if (balance == null) {
                    balanceLayout.visibility = View.GONE
                } else {
                    balanceLayout.visibility = View.VISIBLE
                    balanceValue.text = satoshiToBtc(balance)

                    balanceValue.setTextColor(
                        getAmountColourInt(context!!, balance >= 0)
                    )
                }
            }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadMultiAddress()
    }
}

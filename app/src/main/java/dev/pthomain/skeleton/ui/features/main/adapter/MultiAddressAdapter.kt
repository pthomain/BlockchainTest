package dev.pthomain.skeleton.ui.features.main.adapter

import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import uk.co.glass_software.android.boilerplate.core.base.adapter.BaseRecyclerViewAdapter

class MultiAddressAdapter(
    itemIndexCallback: (Int) -> Unit = { Unit } //only used for paging
) : BaseRecyclerViewAdapter<MultiAddressUiModel, MultiAddressViewHolder>(
    itemIndexCallback = itemIndexCallback
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = MultiAddressViewHolder(parent)

}

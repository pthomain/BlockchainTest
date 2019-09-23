package dev.pthomain.skeleton.ui.features.main.adapter

import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import dev.pthomain.android.boilerplate.core.base.adapter.BaseRecyclerViewAdapter

class MultiAddressAdapter : BaseRecyclerViewAdapter<MultiAddressUiModel, MultiAddressViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = MultiAddressViewHolder(parent)

}

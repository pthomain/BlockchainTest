package dev.pthomain.skeleton.ui.features.main.adapter

import dev.pthomain.skeleton.domain.api.multi_address.models.MultiAddressResponse
import dev.pthomain.skeleton.domain.api.multi_address.models.Txs
import java.util.*

data class MultiAddressUiModel(
    val multiAddress: MultiAddressResponse,
    val txs: Txs,
    val isTemporary: Boolean,
    val cacheDate: Date?
)
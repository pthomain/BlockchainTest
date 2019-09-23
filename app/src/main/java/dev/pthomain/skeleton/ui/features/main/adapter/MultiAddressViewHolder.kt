package dev.pthomain.skeleton.ui.features.main.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import dev.pthomain.skeleton.R
import dev.pthomain.skeleton.ui.utils.*
import uk.co.glass_software.android.boilerplate.core.base.adapter.BaseViewHolder
import uk.co.glass_software.android.boilerplate.core.utils.kotlin.ifElse
import java.math.BigDecimal
import java.util.*

class MultiAddressViewHolder(parent: ViewGroup) : BaseViewHolder<MultiAddressUiModel>(
    parent,
    R.layout.view_multi_address
) {

    private val receivedSentImageView = itemView.findViewById<ImageView>(R.id.sent_received_icon)
    private val bitcoinValueView = itemView.findViewById<TextView>(R.id.bitcoin_value)
    private val hashView = itemView.findViewById<TextView>(R.id.hash)
    private val feeView = itemView.findViewById<TextView>(R.id.fee)
    private val txDateView = itemView.findViewById<TextView>(R.id.tx_date)
    private val cacheDateView = itemView.findViewById<TextView>(R.id.cache_date)

    override fun populate(model: MultiAddressUiModel) {
        val context = itemView.context

        with(model.txs) {
            val value = out.firstOrNull()?.value

            val isPositive = value
                ?.let { BigDecimal(it) }
                ?.compareTo(BigDecimal.ZERO)
                ?.let { it > 0 }
                ?: true

            bitcoinValueView.text = satoshiToBtc(value)
            feeView.text = context.getString(R.string.fee, satoshiToBtc(fee))
            hashView.text = context.getString(R.string.hash, hash)

            val drawable = context.getDrawable(
                ifElse(
                    isPositive,
                    R.drawable.ic_received,
                    R.drawable.ic_sent
                )
            )!!

            drawable.setTint(getAmountColourInt(context, isPositive))

            receivedSentImageView.setImageDrawable(drawable)

            txDateView.text = context.getString(
                R.string.transaction_date,
                simpleDateFormat.format(Date(time * 1000))
            )

            itemView.alpha = ifElse(model.isTemporary, 0.3f, 1f)

            if (model.cacheDate == null) {
                cacheDateView.visibility = View.GONE
            } else {
                cacheDateView.visibility = View.VISIBLE
                cacheDateView.text = context.getString(
                    R.string.cache_date,
                    fullDateFormat.format(model.cacheDate)
                )
            }
        }
    }

}
package dev.pthomain.skeleton.ui.utils

import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import dev.pthomain.skeleton.R
import uk.co.glass_software.android.boilerplate.core.utils.kotlin.ifElse
import java.math.BigDecimal
import java.text.SimpleDateFormat


fun satoshiToBtc(value: Long?) =
    value?.let {
        BigDecimal(it)
            .divide(BigDecimal(100000000L))
            .let { "BTC $it" }
    } ?: "N/A"

@ColorInt
fun getAmountColourInt(
    context: Context,
    isPositive: Boolean
) =
    ContextCompat.getColor(
        context,
        getAmountColourRes(isPositive)
    )

@ColorRes
fun getAmountColourRes(isPositive: Boolean) =
    ifElse(
        isPositive,
        R.color.positive_amount_colour,
        R.color.negative_amount_colour
    )

val simpleDateFormat = SimpleDateFormat("dd MMM yyyy")
val fullDateFormat = SimpleDateFormat("dd/MM/yy H:mm:ss")

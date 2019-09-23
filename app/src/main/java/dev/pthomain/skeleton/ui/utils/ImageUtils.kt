package dev.pthomain.skeleton.ui.utils

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions

private val circleCropRequestOptions = RequestOptions.circleCropTransform()

fun ImageView.loadUrl(requestManager: RequestManager,
                      url: String?,
                      requestOptions: RequestOptions? = circleCropRequestOptions) {
    if (url != null) {
        visibility = View.VISIBLE
        requestManager.load(url).let {
            if (requestOptions != null) it.apply(requestOptions)
            else it
        }.into(this)
    } else {
        visibility = View.INVISIBLE
    }
}
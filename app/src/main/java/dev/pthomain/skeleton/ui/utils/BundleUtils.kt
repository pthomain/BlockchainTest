package dev.pthomain.skeleton.ui.utils

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import dev.pthomain.android.boilerplate.core.utils.bundle.*
import java.io.Serializable

//
// Activity
//

inline fun <reified A : Activity> A.intent(caller: Activity) =
    Intent(caller, A::class.java)

//
// Fragment
//

inline fun <reified T> Fragment.optionalParam(key: String) =
    arguments.optionalParam<T>(key)

inline fun <reified T> Fragment.param(key: String) =
    arguments.param<T>(key)

inline fun <reified T : Parcelable> Fragment.optionalParcelable(key: String) =
    arguments.optionalParcelable<T>(key)

inline fun <reified T : Parcelable> Fragment.parcelable(key: String) =
    arguments.parcelable<T>(key)

inline fun <reified T : Serializable> Fragment.optionalSerializable(key: String) =
    arguments.optionalSerializable<T>(key)

inline fun <reified T : Serializable> Fragment.serializable(key: String) =
    arguments.serializable<T>(key)


//
// Adding parameters
//

fun <T : Fragment> T.addParams(vararg extra: Pair<String, Any>) = apply {
    if (arguments == null) {
        arguments = Bundle()
    }
    arguments!!.putAll(extra.toMap().toBundle())
}

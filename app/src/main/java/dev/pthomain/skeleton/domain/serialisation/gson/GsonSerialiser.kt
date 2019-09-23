package dev.pthomain.skeleton.domain.serialisation.gson

import com.google.gson.Gson
import dev.pthomain.android.dejavu.configuration.SimpleSerialiser

class GsonSerialiser(private val gson: Gson) : SimpleSerialiser() {

    override fun <O : Any> serialise(target: O) =
        gson.toJson(target)!!

    override fun <O> deserialise(
        serialised: String,
        targetClass: Class<O>
    ) = gson.fromJson(serialised, targetClass)!!

}

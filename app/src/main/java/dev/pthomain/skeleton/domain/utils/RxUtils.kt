package dev.pthomain.skeleton.domain.utils

import io.reactivex.Single
import io.reactivex.functions.BiFunction

inline fun <reified A, reified B> Single<A>.zipToPair(otherSingle: Single<B>) =
    zipWith(
        otherSingle,
        BiFunction<A, B, Pair<A, B>> { a, b -> a to b }
    )

package dev.pthomain.skeleton.ui.di

import javax.inject.Scope
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.*


@Scope
@Retention(RUNTIME)
@Target(FUNCTION, CLASS)
annotation class FragmentScope
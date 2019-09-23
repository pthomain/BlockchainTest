package dev.pthomain.skeleton.di

interface ComponentProvider<C> {

    fun getComponent(): C

}
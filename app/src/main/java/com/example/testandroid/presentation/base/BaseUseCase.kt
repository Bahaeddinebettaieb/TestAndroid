package com.example.testandroid.presentation.base

abstract class BaseUseCase<in P, out R> {
    interface Callback<in R> {
        fun onSuccess(result: R)
        fun onError(throwable: Throwable)
    }

    abstract suspend fun execute(state:Int,params: P?, callback: Callback<R>)

    abstract suspend fun execute1(params0: P?, params1: P?, callback: Callback<R>)
}
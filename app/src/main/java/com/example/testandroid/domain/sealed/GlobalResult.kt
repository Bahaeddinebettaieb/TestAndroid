package com.example.testandroid.domain.sealed


sealed class GlobalResult<out T> {
    object LoadingState : GlobalResult<Nothing>()
    data class ErrorState(var exception: Throwable) : GlobalResult<Nothing>()
    data class DataState<T>(var data: T) : GlobalResult<T>()
}
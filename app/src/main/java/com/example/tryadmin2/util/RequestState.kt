package com.example.tryadmin2.util

sealed class RequestState<out T> private constructor() {
    data class Success<out T>(val data: T): RequestState<T>()
    data class Error(val message: String): RequestState<Nothing>()
    object Loading: RequestState<Nothing>()
}
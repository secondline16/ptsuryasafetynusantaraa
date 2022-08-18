package com.ssn.app.vo

sealed class UiState<out T> {
    data class Loading<T>(val data: T? = null) : UiState<T>()
    data class Error(val e: Throwable) : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
}

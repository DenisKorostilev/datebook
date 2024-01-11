package com.example.datebook.domain

sealed class NetworkResult<T> {
    data class Success<T>(val result: T) : NetworkResult<T>()
    data class Error<T>(val errorText: String) : NetworkResult<T>()
}

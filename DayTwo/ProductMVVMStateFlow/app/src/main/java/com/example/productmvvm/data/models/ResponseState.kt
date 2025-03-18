package com.example.productmvvm.data.models


sealed class ResponseState {
    data object Loading : ResponseState()

    data class Success(val data: List<Product>) : ResponseState()

    data class Failure(val error: Throwable) : ResponseState()

}


package com.example.productappmvvm.remote

import com.example.productmvvm.data.models.Product
import kotlinx.coroutines.flow.Flow


interface ProductRemoteDataSource {

    suspend fun getAllProducts(): Flow<List<Product>>?
}
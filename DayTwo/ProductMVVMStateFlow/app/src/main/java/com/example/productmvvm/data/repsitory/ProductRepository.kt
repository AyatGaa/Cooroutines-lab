package com.example.productappmvvm.repositoty


import com.example.productappmvvm.remote.ProductRemoteDataSource
import com.example.productmvvm.data.models.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    suspend fun getAllProduct(isOnline: Boolean): Flow<List<Product>>?

    suspend fun addProductToFavourite(product: Product): Long

    suspend fun removeProductFromFavourite(product: Product): Int


}
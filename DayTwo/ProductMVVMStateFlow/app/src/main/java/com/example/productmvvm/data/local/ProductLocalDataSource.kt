package com.example.productmvvm.data.local

import com.example.productmvvm.data.models.Product
import kotlinx.coroutines.flow.Flow


interface ProductLocalDataSource {

    suspend fun getAllProduct(): Flow<List<Product>>

    suspend fun insertProduct(product: Product):Long

    suspend fun deleteProduct(product: Product):Int


}
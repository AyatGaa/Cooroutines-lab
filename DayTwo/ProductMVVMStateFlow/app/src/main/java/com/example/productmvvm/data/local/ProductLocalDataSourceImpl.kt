package com.example.productmvvm.data.local

import android.util.Log
import com.example.productmvvm.data.models.Product
import kotlinx.coroutines.flow.Flow


class ProductLocalDataSourceImpl(private val dao : ProductDao): ProductLocalDataSource {
    override suspend fun getAllProduct(): Flow<List<Product>> {
        return dao.getLocalProducts()
    }

    override suspend fun insertProduct(product: Product): Long {
        return dao.insertProducts(product)
    }

    override suspend fun deleteProduct(product: Product): Int {
        return dao.deleteProduct(product)
    }
}
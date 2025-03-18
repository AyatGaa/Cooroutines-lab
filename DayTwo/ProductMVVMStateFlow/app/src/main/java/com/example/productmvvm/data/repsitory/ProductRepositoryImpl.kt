package com.example.productappmvvm.repositoty


import android.util.Log
import com.example.productappmvvm.remote.ProductRemoteDataSource
import com.example.productmvvm.data.local.ProductLocalDataSource
import com.example.productmvvm.data.models.Product
import kotlinx.coroutines.flow.Flow

class ProductRepositoryImpl private constructor(
    private val remoteDataSource: ProductRemoteDataSource,
    private val localDataSource: ProductLocalDataSource
) : ProductRepository {

    companion object {
        private var INSTANCE: ProductRepositoryImpl? = null
        fun getInstance(
            remoteDataSource: ProductRemoteDataSource,
            localDataSource: ProductLocalDataSource
        ): ProductRepository {
            return INSTANCE ?: synchronized(this) {
                val inst = ProductRepositoryImpl(remoteDataSource, localDataSource)
                INSTANCE = inst
                inst
            }
        }
    }


    override suspend fun getAllProduct(isOnline: Boolean): Flow<List<Product>>? {
        return if (isOnline) {
            remoteDataSource.getAllProducts()
        } else {
            Log.i("TAG", "getAllProduct: in offline mode")
            localDataSource.getAllProduct()
        }
    }

    override suspend fun addProductToFavourite(product: Product): Long {
        return localDataSource.insertProduct(product)
    }

    override suspend fun removeProductFromFavourite(product: Product): Int {
        return localDataSource.deleteProduct(product)
    }
}
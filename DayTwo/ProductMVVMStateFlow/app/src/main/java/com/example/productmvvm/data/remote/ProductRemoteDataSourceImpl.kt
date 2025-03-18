package com.example.productappmvvm.remote

import android.util.Log
import com.example.productmvvm.data.models.Product
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class ProductRemoteDataSourceImpl(private val service:ProductService) :ProductRemoteDataSource {


    override suspend fun getAllProducts(): Flow<List<Product>> {
        val latestProducts: Flow<List<Product>> = flow {
            while (true) {
                val latestProducts = service.getAllProducts().body()?.products
                Log.w("TAG", "getAllProducts: ${latestProducts?.get(2)}", )

                if (latestProducts != null) {
                    emit(latestProducts)
                    Log.w("TAG", "getAllProducts: ${latestProducts.get(2)}", )
                }

            }
        }
        return latestProducts
    }
}
package com.example.productappmvvm.remote

import retrofit2.Response
import retrofit2.http.GET

interface ProductService {

    // we don't add Flow here as, it the API request, as retrofit is One-Time Request opposite of FLow
    // we can wrap the Response in Flow
    @GET("products")
    suspend fun getAllProducts(): Response<ProductResponse>

}
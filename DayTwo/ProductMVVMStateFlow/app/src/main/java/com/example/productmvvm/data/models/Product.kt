package com.example.productmvvm.data.models


import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "product")
data class Product(
    @PrimaryKey
    val id: Long,
    val title: String,
    val description: String,
    val category: String,
    val price: Double,
    val rating: Double,
    val brand: String?,
    val thumbnail: String,
)
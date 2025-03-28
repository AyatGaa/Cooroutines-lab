package com.example.productmvvm.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.productmvvm.data.models.Product


@Database(entities = [Product::class], version = 2)
abstract class ProductDataBase :RoomDatabase() {

    abstract fun getProductDao(): ProductDao


    companion object{

        private var instance: ProductDataBase? =null

        fun getInstance(context:Context): ProductDataBase {
            return instance ?: synchronized(this){
                val INSTANCE = Room.databaseBuilder(context, ProductDataBase::class.java, "product").build()

                instance = INSTANCE
                INSTANCE
            }
        }
    }

}
package com.example.project

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product)

    @Query("SELECT * FROM product_table")
    fun getAllProducts(): LiveData<List<Product>>

    @Delete
    suspend fun deleteproduct(product: Product)

    @Update
    suspend fun updateproduct(product: Product)
}

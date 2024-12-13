package com.barleyan.managementoko

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

    @Query("SELECT * FROM product_table")
    fun getAllProducts(): LiveData<List<Product>>

    @Update
    suspend fun updateProduct(product: Product)
}
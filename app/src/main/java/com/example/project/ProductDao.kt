package com.barleyan.managementoko

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.project.Product

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product)

    @Query("SELECT * FROM product_table")
    fun getAllProducts(): LiveData<List<Product>>

    @Query("SELECT MAX(id) FROM product_table")
    suspend fun getMaxIdProduct(): Int?

    @Delete
    suspend fun deleteProduct(product: Product)

    @Update
    suspend fun updateProduct(product: Product)
}
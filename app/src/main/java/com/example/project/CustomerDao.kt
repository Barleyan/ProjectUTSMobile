package com.barleyan.managementoko

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.*

@Dao
interface CustomerDao {

    @Insert
    suspend fun insertCustomer(customer: Customer)

    @Update
    suspend fun update(customer: Customer)

    @Delete
    suspend fun delete(customer: Customer)

    @Query("SELECT * FROM customer_table")
    fun getAllCustomers(): LiveData<List<Customer>>
}
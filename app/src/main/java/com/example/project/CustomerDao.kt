package com.barleyan.managementoko

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface CustomerDao {

    // Insert a customer, replacing if already exists
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: Customer)

    // Get all customers as LiveData
    @Query("SELECT * FROM customer_table")
    fun getAllCustomers(): LiveData<List<Customer>>

    // Get all customers as an array (added in your code)
    @Query("SELECT * FROM customer_table")
    fun getAll(): Array<Customer>

    // Get the maximum customer ID
    @Query("SELECT MAX(id) FROM customer_table")
    suspend fun getMaxId(): Int?

    // Delete a customer
    @Delete
    suspend fun delete(customer: Customer)

    // Update a customer
    @Update
    suspend fun update(customer: Customer)
}
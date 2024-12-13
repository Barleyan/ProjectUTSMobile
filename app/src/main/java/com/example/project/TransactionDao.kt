package com.barleyan.managementoko

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TransactionDao {

    // Insert a transaction, replace if there's a conflict
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    // Delete a transaction
    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    // Get all transactions as LiveData
    @Query("SELECT * FROM transaction_table")
    fun getAllTransactions(): LiveData<List<Transaction>>

    // Get the maximum id from the transaction_table
    @Query("SELECT MAX(id) FROM transaction_table")
    suspend fun getMaxIdTransaction(): Int?

    // Update a transaction
    @Update
    suspend fun updateTransaction(transaction: Transaction)
}
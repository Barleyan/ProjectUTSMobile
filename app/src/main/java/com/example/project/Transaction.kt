package com.barleyan.managementoko

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_table")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productId: String,
    val customerId: String,
    val quantity: String,
    val totalPrice: String,
    val amount: Double
)

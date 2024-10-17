package com.example.project

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_table")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: Int,
    val customerId: Int,
    val quantity: Int,
    val totalPrice: Double
)


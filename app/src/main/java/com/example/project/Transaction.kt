package com.example.project

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_table")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var productId: String,
    var customerId: String,
    var quantity: String,
    var totalPrice: String,
)


package com.barleyan.managementoko

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_table")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var productId: Int = 0,      // Changed to Int if referring to Product ID
    var customerId: Int = 0,     // Changed to Int if referring to Customer ID
    var quantity: Int = 0,       // Changed to Int for quantity
    var totalPrice: Int = 0      // Changed to Int for totalPrice
)
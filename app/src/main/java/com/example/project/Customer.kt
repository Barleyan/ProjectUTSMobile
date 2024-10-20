package com.example.project

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customer_table")
data class Customer(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var name: String,
    var phoneNumber: String
)


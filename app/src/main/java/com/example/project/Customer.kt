package com.barleyan.managementoko

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customer_table")
data class Customer(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String = "",
    var phoneNumber: String = ""
) {
    // Default constructor for Room Database
    constructor() : this(0, "", "")
}
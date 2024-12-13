package com.example.project

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_table")
data class Product(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String = "",   // Fixed 'name2' to 'name'
    var price: String = "",
    var stock: String = ""
) {
    constructor() : this(0, "", "", "")
}
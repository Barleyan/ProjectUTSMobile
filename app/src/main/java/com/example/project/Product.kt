package com.barleyan.managementoko

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_table")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var name: String = "",
    var price: String = "",
    var stock: String = ""
)
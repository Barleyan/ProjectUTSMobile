package com.example.project

import androidx.room.Entity
import androidx.room.PrimaryKey

// Mendefinisikan entitas untuk tabel "customer_table" di Room Database
@Entity(tableName = "customer_table")
data class Customer(
    // ID utama untuk entitas, yang akan dibuat secara otomatis oleh Room
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    // Nama pelanggan
    var name: String = "",

    // Nomor telepon pelanggan
    var phoneNumber: String = ""
) {
    // Konstruktor kosong untuk inisialisasi default
    constructor() : this(0, "", "")
}
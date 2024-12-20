package com.example.project

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Mendefinisikan database Room dengan tiga entitas: Product, Customer, dan Transaction
@Database(entities = [Product::class, Customer::class, Transaction::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    // Menyediakan akses ke DAO untuk entitas Product
    abstract fun productDao(): ProductDao

    // Menyediakan akses ke DAO untuk entitas Customer
    abstract fun customerDao(): CustomerDao

    // Menyediakan akses ke DAO untuk entitas Transaction
    abstract fun transactionDao(): TransactionDao

    companion object {
        // Instance tunggal database
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Fungsi untuk mendapatkan instance database
        fun getDatabase(context: Context): AppDatabase {
            // Mengecek apakah INSTANCE sudah ada, jika tidak membuat instance baru
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "db_elektronik" // Nama database
                ).build()
                // Menyimpan instance yang dibuat ke dalam INSTANCE
                INSTANCE = instance
                instance
            }
        }
    }
}
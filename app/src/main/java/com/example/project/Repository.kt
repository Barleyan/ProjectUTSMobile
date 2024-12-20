package com.example.project

import androidx.lifecycle.LiveData
import androidx.room.Dao

class Repository(
    private val productDao: ProductDao,
    private val customerDao: CustomerDao,
    private val transactionDao: TransactionDao
) {

    // Mendapatkan semua data produk dari ProductDao
    val allProducts: LiveData<List<Product>> = productDao.getAllProducts()

    // Mendapatkan semua data pelanggan dari CustomerDao
    val allCustomers: LiveData<List<Customer>> = customerDao.getAllCustomers()

    // Mendapatkan semua data transaksi dari TransactionDao
    val allTransactions: LiveData<List<Transaction>> = transactionDao.getAllTransactions()

    // Fungsi untuk memasukkan produk baru
    suspend fun insertProduct(product: Product) {
        productDao.insertProduct(product)  // Memanggil method untuk memasukkan produk
    }

    // Fungsi untuk memasukkan pelanggan baru
    suspend fun insertCustomer(customer: Customer) {
        customerDao.insertCustomer(customer)  // Memanggil method untuk memasukkan pelanggan
    }

    // Fungsi untuk memperbarui data pelanggan
    suspend fun updateCustomer(customer: Customer) {
        customerDao.update(customer)  // Memanggil method untuk memperbarui pelanggan
    }

    // Fungsi untuk menghapus data pelanggan
    suspend fun delete(customer: Customer) {
        customerDao.delete(customer)  // Memanggil method untuk menghapus pelanggan
    }

    // Fungsi untuk mendapatkan ID pelanggan maksimum
    suspend fun getMaxCustomerId(): Int? {
        return customerDao.getMaxId()  // Mengambil ID maksimum pelanggan dari DAO
    }

    // Fungsi untuk mendapatkan ID produk maksimum
    suspend fun getMaxProductId(): Int? {
        return productDao.getMaxIdProduct()  // Mengambil ID maksimum produk dari DAO
    }

    // Fungsi untuk mendapatkan ID transaksi maksimum
    suspend fun getMaxTransactionId(): Int? {
        return transactionDao.getMaxIdTransaction()  // Mengambil ID maksimum transaksi dari DAO
    }

    // Fungsi untuk menghapus produk
    suspend fun delete(product: Product) {
        productDao.deleteproduct(product)  // Memanggil method untuk menghapus produk
    }

    // Fungsi untuk memperbarui data produk
    suspend fun updateProduct(product: Product) {
        productDao.updateproduct(product)  // Memanggil method untuk memperbarui produk
    }

    // Fungsi untuk memasukkan transaksi baru
    suspend fun insertTransaction(transaction: Transaction) {
        transactionDao.insertTransaction(transaction)  // Memanggil method untuk memasukkan transaksi
    }

    // Fungsi untuk menghapus transaksi
    suspend fun delete(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction)  // Memanggil method untuk menghapus transaksi
    }

    // Fungsi untuk memperbarui data transaksi
    suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.updateTransaction(transaction)  // Memanggil method untuk memperbarui transaksi
    }
}
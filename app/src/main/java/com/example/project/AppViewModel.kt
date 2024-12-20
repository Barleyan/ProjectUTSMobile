package com.example.project

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// ViewModel untuk mengelola data antara UI dan database
class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: Repository
    val allCustomers: LiveData<List<Customer>> // LiveData untuk daftar pelanggan
    val allProducts: LiveData<List<Product>> // LiveData untuk daftar produk
    val allTransactions: LiveData<List<Transaction>> // LiveData untuk daftar transaksi

    // Referensi untuk Firebase Realtime Database
    private val firebaseDb = FirebaseDatabase.getInstance()
    private val productsRef = firebaseDb.getReference("products")
    private val customersRef = firebaseDb.getReference("customers")
    private val transactionsRef = firebaseDb.getReference("transactions")

    init {
        // Menginisialisasi DAO dan Repository
        val productDao = AppDatabase.getDatabase(application).productDao()
        val customerDao = AppDatabase.getDatabase(application).customerDao()
        val transactionDao = AppDatabase.getDatabase(application).transactionDao()
        repository = Repository(productDao, customerDao, transactionDao)

        // Mengambil data dari Repository
        allCustomers = repository.allCustomers
        allProducts = repository.allProducts
        allTransactions = repository.allTransactions
    }

    // Fungsi untuk menambahkan pelanggan baru ke database lokal dan sinkronisasi ke Firebase
    fun insertCustomer(customer: Customer) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val currentMaxId = repository.getMaxCustomerId() ?: 0
            customer.id = currentMaxId + 1 // Mengatur ID pelanggan

            repository.insertCustomer(customer) // Menyimpan data di database lokal
            customersRef.child(customer.id.toString()).setValue(customer) // Sinkronisasi ke Firebase
                .addOnSuccessListener { println("Customer inserted and synced with Firebase: ${customer.id}") }
                .addOnFailureListener { e -> println("Error syncing customer to Firebase: ${e.message}") }
        } catch (e: Exception) {
            println("Error inserting customer: ${e.message}")
        }
    }

    // Fungsi untuk menghapus pelanggan dari database lokal dan Firebase
    fun deleteCustomer(customer: Customer) = viewModelScope.launch(Dispatchers.IO) {
        try {
            repository.delete(customer) // Menghapus data di database lokal
            customersRef.child(customer.id.toString()).removeValue() // Menghapus data di Firebase
                .addOnSuccessListener { println("Customer deleted from Firebase: ${customer.id}") }
                .addOnFailureListener { e -> println("Error deleting customer from Firebase: ${e.message}") }
        } catch (e: Exception) {
            println("Error deleting customer: ${e.message}")
        }
    }

    // Fungsi untuk memperbarui data pelanggan di database lokal dan Firebase
    fun updateCustomer(customer: Customer) = viewModelScope.launch(Dispatchers.IO) {
        try {
            repository.updateCustomer(customer) // Memperbarui data di database lokal
            customersRef.child(customer.id.toString()).setValue(customer) // Sinkronisasi ke Firebase
                .addOnSuccessListener { println("Customer updated and synced with Firebase: ${customer.id}") }
                .addOnFailureListener { e -> println("Error syncing updated customer to Firebase: ${e.message}") }
        } catch (e: Exception) {
            println("Error updating customer: ${e.message}")
        }
    }

    // Fungsi untuk menambahkan produk baru ke database lokal dan sinkronisasi ke Firebase
    fun insertProduct(product: Product) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val currentMaxId = repository.getMaxProductId() ?: 0
            product.id = currentMaxId + 1 // Mengatur ID produk

            repository.insertProduct(product) // Menyimpan data di database lokal
            productsRef.child(product.id.toString()).setValue(product) // Sinkronisasi ke Firebase
                .addOnSuccessListener { println("Product inserted and synced with Firebase: ${product.id}") }
                .addOnFailureListener { e -> println("Error syncing product to Firebase: ${e.message}") }
        } catch (e: Exception) {
            println("Error inserting product: ${e.message}")
        }
    }

    // Fungsi untuk menghapus produk dari database lokal dan Firebase
    fun deleteProduct(product: Product) = viewModelScope.launch(Dispatchers.IO) {
        try {
            repository.delete(product) // Menghapus data di database lokal
            productsRef.child(product.id.toString()).removeValue() // Menghapus data di Firebase
                .addOnSuccessListener { println("Product deleted from Firebase: ${product.id}") }
                .addOnFailureListener { e -> println("Error deleting product from Firebase: ${e.message}") }
        } catch (e: Exception) {
            println("Error deleting product: ${e.message}")
        }
    }

    // Fungsi untuk memperbarui data produk di database lokal dan Firebase
    fun updateProduct(product: Product) = viewModelScope.launch(Dispatchers.IO) {
        try {
            repository.updateProduct(product) // Memperbarui data di database lokal
            productsRef.child(product.id.toString()).setValue(product) // Sinkronisasi ke Firebase
                .addOnSuccessListener { println("Product updated and synced with Firebase: ${product.id}") }
                .addOnFailureListener { e -> println("Error syncing updated product to Firebase: ${e.message}") }
        } catch (e: Exception) {
            println("Error updating product: ${e.message}")
        }
    }

    // Fungsi untuk menambahkan transaksi baru ke database lokal dan sinkronisasi ke Firebase
    fun insertTransaction(transaction: Transaction) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val currentMaxId = repository.getMaxTransactionId() ?: 0
            transaction.id = currentMaxId + 1 // Mengatur ID transaksi

            repository.insertTransaction(transaction) // Menyimpan data di database lokal
            transactionsRef.child(transaction.id.toString()).setValue(transaction) // Sinkronisasi ke Firebase
                .addOnSuccessListener { println("Transaction inserted and synced with Firebase: ${transaction.id}") }
                .addOnFailureListener { e -> println("Error syncing transaction to Firebase: ${e.message}") }
        } catch (e: Exception) {
            println("Error inserting transaction: ${e.message}")
        }
    }

    // Fungsi untuk menghapus transaksi dari database lokal dan Firebase
    fun deleteTransaction(transaction: Transaction) = viewModelScope.launch(Dispatchers.IO) {
        try {
            repository.delete(transaction) // Menghapus data di database lokal
            transactionsRef.child(transaction.id.toString()).removeValue() // Menghapus data di Firebase
                .addOnSuccessListener { println("Transaction deleted from Firebase: ${transaction.id}") }
                .addOnFailureListener { e -> println("Error deleting transaction from Firebase: ${e.message}") }
        } catch (e: Exception) {
            println("Error deleting transaction: ${e.message}")
        }
    }

    // Fungsi untuk memperbarui data transaksi di database lokal dan Firebase
    fun updateTransaction(transaction: Transaction) = viewModelScope.launch(Dispatchers.IO) {
        try {
            repository.updateTransaction(transaction) // Memperbarui data di database lokal
            transactionsRef.child(transaction.id.toString()).setValue(transaction) // Sinkronisasi ke Firebase
                .addOnSuccessListener { println("Transaction updated and synced with Firebase: ${transaction.id}") }
                .addOnFailureListener { e -> println("Error syncing updated transaction to Firebase: ${e.message}") }
        } catch (e: Exception) {
            println("Error updating transaction: ${e.message}")
        }
    }
}
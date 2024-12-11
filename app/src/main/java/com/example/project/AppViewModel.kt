package com.example.project

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: Repository
    val allCustomers: LiveData<List<Customer>>
    val allProducts: LiveData<List<Product>>
    val allTransactions: LiveData<List<Transaction>>

    // Firebase Realtime Database references
    private val firebaseDb = FirebaseDatabase.getInstance()
    private val productsRef = firebaseDb.getReference("products")
    private val customersRef = firebaseDb.getReference("customers")
    private val transactionsRef = firebaseDb.getReference("transactions")

    init {
        val productDao = AppDatabase.getDatabase(application).productDao()
        val customerDao = AppDatabase.getDatabase(application).customerDao()
        val transactionDao = AppDatabase.getDatabase(application).transactionDao()
        repository = Repository(productDao, customerDao, transactionDao)

        allCustomers = repository.allCustomers
        allProducts = repository.allProducts
        allTransactions = repository.allTransactions
    }

    // Room Operations with Firebase Sync
    fun insertCustomer(customer: Customer) = viewModelScope.launch(Dispatchers.IO) {
        try {
            repository.insertCustomer(customer)
            customersRef.child(customer.id.toString()).setValue(customer)
                .addOnSuccessListener { println("Customer inserted and synced with Firebase: ${customer.id}") }
                .addOnFailureListener { e -> println("Error syncing customer to Firebase: ${e.message}") }
        } catch (e: Exception) {
            println("Error inserting customer: ${e.message}")
        }
    }

    fun deleteCustomer(customer: Customer) = viewModelScope.launch(Dispatchers.IO) {
        try {
            repository.delete(customer)
            customersRef.child(customer.id.toString()).removeValue()
                .addOnSuccessListener { println("Customer deleted from Firebase: ${customer.id}") }
                .addOnFailureListener { e -> println("Error deleting customer from Firebase: ${e.message}") }
        } catch (e: Exception) {
            println("Error deleting customer: ${e.message}")
        }
    }

    fun updateCustomer(customer: Customer) = viewModelScope.launch(Dispatchers.IO) {
        try {
            repository.updateCustomer(customer)
            customersRef.child(customer.id.toString()).setValue(customer)
                .addOnSuccessListener { println("Customer updated and synced with Firebase: ${customer.id}") }
                .addOnFailureListener { e -> println("Error syncing updated customer to Firebase: ${e.message}") }
        } catch (e: Exception) {
            println("Error updating customer: ${e.message}")
        }
    }

    fun insertProduct(product: Product) = viewModelScope.launch(Dispatchers.IO) {
        try {
            repository.insertProduct(product)
            productsRef.child(product.id.toString()).setValue(product)
                .addOnSuccessListener { println("Product inserted and synced with Firebase: ${product.id}") }
                .addOnFailureListener { e -> println("Error syncing product to Firebase: ${e.message}") }
        } catch (e: Exception) {
            println("Error inserting product: ${e.message}")
        }
    }

    fun deleteProduct(product: Product) = viewModelScope.launch(Dispatchers.IO) {
        try {
            repository.delete(product)
            productsRef.child(product.id.toString()).removeValue()
                .addOnSuccessListener { println("Product deleted from Firebase: ${product.id}") }
                .addOnFailureListener { e -> println("Error deleting product from Firebase: ${e.message}") }
        } catch (e: Exception) {
            println("Error deleting product: ${e.message}")
        }
    }

    fun updateProduct(product: Product) = viewModelScope.launch(Dispatchers.IO) {
        try {
            repository.updateProduct(product)
            productsRef.child(product.id.toString()).setValue(product)
                .addOnSuccessListener { println("Product updated and synced with Firebase: ${product.id}") }
                .addOnFailureListener { e -> println("Error syncing updated product to Firebase: ${e.message}") }
        } catch (e: Exception) {
            println("Error updating product: ${e.message}")
        }
    }

    fun insertTransaction(transaction: Transaction) = viewModelScope.launch(Dispatchers.IO) {
        try {
            repository.insertTransaction(transaction)
            transactionsRef.child(transaction.id.toString()).setValue(transaction)
                .addOnSuccessListener { println("Transaction inserted and synced with Firebase: ${transaction.id}") }
                .addOnFailureListener { e -> println("Error syncing transaction to Firebase: ${e.message}") }
        } catch (e: Exception) {
            println("Error inserting transaction: ${e.message}")
        }
    }

    fun deleteTransaction(transaction: Transaction) = viewModelScope.launch(Dispatchers.IO) {
        try {
            repository.delete(transaction)
            transactionsRef.child(transaction.id.toString()).removeValue()
                .addOnSuccessListener { println("Transaction deleted from Firebase: ${transaction.id}") }
                .addOnFailureListener { e -> println("Error deleting transaction from Firebase: ${e.message}") }
        } catch (e: Exception) {
            println("Error deleting transaction: ${e.message}")
        }
    }

    fun updateTransaction(transaction: Transaction) = viewModelScope.launch(Dispatchers.IO) {
        try {
            repository.updateTransaction(transaction)
            transactionsRef.child(transaction.id.toString()).setValue(transaction)
                .addOnSuccessListener { println("Transaction updated and synced with Firebase: ${transaction.id}") }
                .addOnFailureListener { e -> println("Error syncing updated transaction to Firebase: ${e.message}") }
        } catch (e: Exception) {
            println("Error updating transaction: ${e.message}")
        }
    }
}

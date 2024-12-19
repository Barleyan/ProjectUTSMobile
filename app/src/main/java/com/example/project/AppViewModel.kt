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

    // Room Operations
    fun insertCustomer(customer: Customer) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertCustomer(customer)
        saveCustomerToFirebase(customer) // Sync with Firebase
    }

    fun deleteCustomer(customer: Customer) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(customer)
        deleteCustomerFromFirebase(customer.name) // Remove from Firebase
    }

    fun insertProduct(product: Product) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertProduct(product)
        saveProductToFirebase(product) // Sync with Firebase
    }

    fun deleteProduct(product: Product) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(product)
        deleteProductFromFirebase(product.name2) // Remove from Firebase
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

    fun insertTransaction(transaction: Transaction) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertTransaction(transaction)
        saveTransactionToFirebase(transaction) // Sync with Firebase
    }

    fun deleteTransaction(transaction: Transaction) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(transaction)
        deleteTransactionFromFirebase(transaction.quantity) // Remove from Firebase
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

    // Firebase Operations
    private fun saveProductToFirebase(product: Product) {
        productsRef.child(product.name2).setValue(product)
            .addOnSuccessListener { println("Product saved to Firebase successfully") }
            .addOnFailureListener { e -> println("Error saving product: ${e.message}") }
    }

    private fun deleteProductFromFirebase(productId: String) {
        productsRef.child(productId).removeValue()
            .addOnSuccessListener { println("Product deleted from Firebase successfully") }
            .addOnFailureListener { e -> println("Error deleting product: ${e.message}") }
    }

    private fun saveCustomerToFirebase(customer: Customer) {
        customersRef.child(customer.name).setValue(customer)
            .addOnSuccessListener { println("Customer saved to Firebase successfully") }
            .addOnFailureListener { e -> println("Error saving customer: ${e.message}") }
    }

    private fun deleteCustomerFromFirebase(customerId: String) {
        customersRef.child(customerId).removeValue()
            .addOnSuccessListener { println("Customer deleted from Firebase successfully") }
            .addOnFailureListener { e -> println("Error deleting customer: ${e.message}") }
    }

    private fun saveTransactionToFirebase(transaction: Transaction) {
        transactionsRef.child(transaction.quantity).setValue(transaction)
            .addOnSuccessListener { println("Transaction saved to Firebase successfully") }
            .addOnFailureListener { e -> println("Error saving transaction: ${e.message}") }
    }

    private fun deleteTransactionFromFirebase(transactionId: String) {
        transactionsRef.child(transactionId).removeValue()
            .addOnSuccessListener { println("Transaction deleted from Firebase successfully") }
            .addOnFailureListener { e -> println("Error deleting transaction: ${e.message}") }
    }
}

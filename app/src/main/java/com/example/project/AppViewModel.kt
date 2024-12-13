package com.barleyan.managementoko

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.project.Product
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository
    val allCustomers: LiveData<List<Customer>>
    val allProducts: LiveData<List<Product>>
    val allTransactions: LiveData<List<Transaction>>

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

    // Insert customer and save to Firebase
    fun insertCustomer(customer: Customer) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertCustomer(customer)
        saveToFirebase(customersRef, customer.id.toString(), customer)
    }

    // Delete customer and remove from Firebase
    fun deleteCustomer(customer: Customer) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteCustomer(customer)
        deleteFromFirebase(customersRef, customer.id.toString())
    }

    // Update customer and sync with Firebase
    fun updateCustomer(customer: Customer) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateCustomer(customer)
        saveToFirebase(customersRef, customer.id.toString(), customer)
    }

    // Insert product and save to Firebase
    fun insertProduct(product: Product) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertProduct(product)
        saveToFirebase(productsRef, product.id.toString(), product)
    }

    // Delete product and remove from Firebase
    fun deleteProduct(product: Product) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteProduct(product)
        deleteFromFirebase(productsRef, product.id.toString())
    }

    // Update product and sync with Firebase
    fun updateProduct(product: Product) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateProduct(product)
        saveToFirebase(productsRef, product.id.toString(), product)
    }

    // Insert transaction and save to Firebase
    fun insertTransaction(transaction: Transaction) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertTransaction(transaction)
        saveToFirebase(transactionsRef, transaction.id.toString(), transaction)
    }

    // Delete transaction and remove from Firebase
    fun deleteTransaction(transaction: Transaction) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteTransaction(transaction)
        deleteFromFirebase(transactionsRef, transaction.id.toString())
    }

    // Update transaction and sync with Firebase
    fun updateTransaction(transaction: Transaction) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateTransaction(transaction)
        saveToFirebase(transactionsRef, transaction.id.toString(), transaction)
    }

    // Helper method to save data to Firebase
    private fun <T> saveToFirebase(ref: DatabaseReference, key: String, data: T) {
        ref.child(key).setValue(data)
            .addOnSuccessListener {
                Log.d("AppViewModel", "Data berhasil disimpan ke Firebase")
            }
            .addOnFailureListener { exception ->
                Log.e("AppViewModel", "Gagal menyimpan data ke Firebase", exception)
            }
    }

    // Helper method to delete data from Firebase
    private fun deleteFromFirebase(ref: DatabaseReference, key: String) {
        ref.child(key).removeValue()
            .addOnSuccessListener {
                Log.d("AppViewModel", "Data berhasil dihapus dari Firebase")
            }
            .addOnFailureListener { exception ->
                Log.e("AppViewModel", "Gagal menghapus data dari Firebase", exception)
            }
    }
}
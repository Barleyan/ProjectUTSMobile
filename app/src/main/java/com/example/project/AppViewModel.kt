package com.example.project

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: Repository
    val allCustomers: LiveData<List<Customer>>
    val allProducts: LiveData<List<Product>>
    val allTransactions: LiveData<List<Transaction>>

    init {
        val productDao = AppDatabase.getDatabase(application).productDao()
        val customerDao = AppDatabase.getDatabase(application).customerDao()
        val transactionDao = AppDatabase.getDatabase(application).transactionDao()
        repository = Repository(productDao, customerDao, transactionDao)

        allCustomers = repository.allCustomers
        allProducts = repository.allProducts
        allTransactions = repository.allTransactions
    }

    fun insertCustomer(customer: Customer) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertCustomer(customer)
    }

    fun insertProduct(product: Product) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertProduct(product)
    }

    fun insertTransaction(transaction: Transaction) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertTransaction(transaction)
    }

    fun deleteCustomer(customer: Customer) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(customer)
    }
}

package com.example.project

import androidx.lifecycle.LiveData
import androidx.room.Dao

class Repository(private val productDao: ProductDao, private val customerDao: CustomerDao, private val transactionDao: TransactionDao) {

    val allProducts: LiveData<List<Product>> = productDao.getAllProducts()
    val allCustomers: LiveData<List<Customer>> = customerDao.getAllCustomers()
    val allTransactions: LiveData<List<Transaction>> = transactionDao.getAllTransactions()

    suspend fun insertProduct(product: Product) {
        productDao.insertProduct(product)
    }

    suspend fun insertCustomer(customer: Customer) {
        customerDao.insertCustomer(customer)
    }
    suspend fun updateCustomer(customer: Customer) {
        customerDao.update(customer)
    }
    suspend fun delete(customer: Customer) {
        customerDao.delete(customer)
    }
    suspend fun delete(product: Product) {
        productDao.deleteproduct(product)
    }
    suspend fun updateProduct(product: Product) {
        productDao.updateproduct(product)
    }
    suspend fun insertTransaction(transaction: Transaction) {
        transactionDao.insertTransaction(transaction)
    }
    suspend fun delete(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction)
    }
    suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.updateTransaction(transaction)
    }
}



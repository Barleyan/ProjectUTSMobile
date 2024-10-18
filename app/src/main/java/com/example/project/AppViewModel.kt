package com.example.project

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AppViewModel : ViewModel() {

    private val _allCustomers = MutableLiveData<List<Customer>>()
    val allCustomers: LiveData<List<Customer>> get() = _allCustomers

    private val customerList = mutableListOf<Customer>()

    init {
        _allCustomers.value = customerList
    }

    fun insertCustomer(customer: Customer) {
        customerList.add(customer)
        _allCustomers.value = customerList // Memperbarui LiveData
    }

    // Contoh metode untuk produk dan transaksi
    fun insertProduct(product: Product) {
        // Logika untuk menyimpan produk
    }

    fun insertTransaction(transaction: Transaction) {
        // Logika untuk menyimpan transaksi
    }
}

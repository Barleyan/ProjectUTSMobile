package com.example.project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.barleyan.managementoko.R

class MainActivity : AppCompatActivity() {

    private lateinit var appViewModel: AppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)

        // Observing data and updating UI
        appViewModel.allProducts.observe(this, Observer { products ->
        })

        appViewModel.allCustomers.observe(this, Observer { customers ->
        })

        appViewModel.allTransactions.observe(this, Observer { transactions ->
        })

        // Insert sample data
        val sampleProduct = Product(name = "Laptop", price = 15000.0, stock = 10)
        appViewModel.insertProduct(sampleProduct)

        val sampleCustomer = Customer(name = "John Doe", phoneNumber = "123456789")
        appViewModel.insertCustomer(sampleCustomer)

        val sampleTransaction = Transaction(productId = 1, customerId = 1, quantity = 1, totalPrice = 15000.0)
        appViewModel.insertTransaction(sampleTransaction)
    }
}

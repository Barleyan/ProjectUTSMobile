package com.barleyan.managementoko

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.AppViewModel
import com.example.project.Customer
import com.example.project.Product
import com.example.project.Transaction
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var appViewModel: AppViewModel
    private lateinit var customerAdapter: CustomerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize adapter with empty list
        customerAdapter = CustomerAdapter(emptyList())
        recyclerView.adapter = customerAdapter

        appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)

        // Observing data and updating UI
        appViewModel.allCustomers.observe(this, Observer { customers ->
            customers?.let {
                customerAdapter.updateCustomers(it) // Update RecyclerView with customer data
            }
        })

        // Tombol FloatingActionButton untuk menambah data
        val fabAdd: FloatingActionButton = findViewById(R.id.fabAdd)
        fabAdd.setOnClickListener {
            showAddCustomerDialog() // Tampilkan dialog untuk menambah customer
        }

        // Insert some sample data (optional)
        val sampleCustomer = Customer(name = "NAMA", phoneNumber = "TELEPON")
        appViewModel.insertCustomer(sampleCustomer)

        val sampleProduct = Product(name = "Laptop", price = 15000.0, stock = 10)
        appViewModel.insertProduct(sampleProduct)

        val sampleTransaction = Transaction(productId = 1, customerId = 1, quantity = 1, totalPrice = 15000.0)
        appViewModel.insertTransaction(sampleTransaction)
    }

    // Fungsi untuk menampilkan dialog penambahan customer baru
    private fun showAddCustomerDialog() {
        // Inflating the layout untuk dialog
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_customer, null)
        val customerNameInput = dialogView.findViewById<EditText>(R.id.etName)
        val customerPhoneInput = dialogView.findViewById<EditText>(R.id.etPhone)

        // Membuat dialog
        AlertDialog.Builder(this)
            .setTitle("Tambah Client")
            .setView(dialogView)
            .setPositiveButton("Tambah", DialogInterface.OnClickListener { dialog, which ->
                // Ketika tombol "Add" ditekan, ambil input dan simpan data baru
                val name = customerNameInput.text.toString()
                val phoneNumber = customerPhoneInput.text.toString()

                if (name.isNotEmpty() && phoneNumber.isNotEmpty()) {
                    val newCustomer = Customer(name = name, phoneNumber = phoneNumber)
                    appViewModel.insertCustomer(newCustomer) // Menyimpan data ke ViewModel
                }
            })
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }
}

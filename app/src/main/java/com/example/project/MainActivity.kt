package com.barleyan.managementoko

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.AppViewModel
import com.example.project.Customer
import com.example.project.CustomerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    lateinit var appViewModel: AppViewModel
    private lateinit var customerAdapter: CustomerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize adapter with empty list
        customerAdapter = CustomerAdapter(mutableListOf())
        recyclerView.adapter = customerAdapter

        // Initialize ViewModel
        appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)

        // Observing data and updating UI
        appViewModel.allCustomers.observe(this, Observer { customers ->
            customers?.let {
                customerAdapter.updateCustomers(it) // Update RecyclerView with customer data
            }
        })

        // FloatingActionButton for adding a new customer
        val fabAdd: FloatingActionButton = findViewById(R.id.fabAdd)
        fabAdd.setOnClickListener {
            showAddCustomerDialog() // Show dialog to add customer
        }
    }

    // Function to show dialog for adding a new customer
    private fun showAddCustomerDialog() {
        // Inflate the layout for the dialog
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_customer, null)
        val customerNameInput = dialogView.findViewById<EditText>(R.id.etName)
        val customerPhoneInput = dialogView.findViewById<EditText>(R.id.etPhone)

        // Create the dialog
        val dialog = AlertDialog.Builder(this)
            .setTitle("Tambah Client")
            .setView(dialogView)
            .setPositiveButton("Tambah") { _, _ -> }
            .setNegativeButton("Batal", null)
            .create()

        // Show the dialog
        dialog.show()

        // Override positive button click to handle validation
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val name = customerNameInput.text.toString().trim()
            val phoneNumber = customerPhoneInput.text.toString().trim()

            // Input validation
            if (name.isNotEmpty() && phoneNumber.isNotEmpty()) {
                // Save the new customer and dismiss dialog
                val newCustomer = Customer(name = name, phoneNumber = phoneNumber)
                appViewModel.insertCustomer(newCustomer) // Save data to ViewModel
                dialog.dismiss() // Close the dialog after saving
            } else {
                // Show error messages if inputs are invalid
                if (name.isEmpty()) customerNameInput.error = "Nama tidak boleh kosong"
                if (phoneNumber.isEmpty()) customerPhoneInput.error = "Nomor telepon tidak boleh kosong"
            }
        }
    }

    // Private function to delete a customer
    private fun deleteCustomer(customer: Customer) {
        appViewModel.deleteCustomer(customer) // Call delete function in ViewModel
        Toast.makeText(this, "${customer.name} has been deleted", Toast.LENGTH_SHORT).show()
    }
}
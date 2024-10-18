package com.barleyan.managementoko

import CustomerAdapter
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

        appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)

        // Initialize adapter with delete functionality
        customerAdapter = CustomerAdapter(emptyList()) { customer ->
            showDeleteConfirmationDialog(customer) // Show confirmation dialog before deleting
        }
        recyclerView.adapter = customerAdapter

        // Observing data and updating UI
        appViewModel.allCustomers.observe(this, Observer { customers ->
            customers?.let {
                customerAdapter.updateCustomers(it) // Update RecyclerView with customer data
            }
        })

        // FloatingActionButton to add new customers
        val fabAdd: FloatingActionButton = findViewById(R.id.fabAdd)
        fabAdd.setOnClickListener {
            showAddCustomerDialog() // Display dialog to add customer
        }
    }

    private fun showDeleteConfirmationDialog(customer: Customer) {
        AlertDialog.Builder(this)
            .setTitle("Delete Customer")
            .setMessage("Are you sure you want to delete ${customer.name}?")
            .setPositiveButton("Yes") { dialog, _ ->
                appViewModel.deleteCustomer(customer) // Delete the customer
                dialog.dismiss()
            }
            .setNegativeButton("No", null)
            .create()
            .show()
    }

    // Function to show add customer dialog (as before)
    private fun showAddCustomerDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_customer, null)
        val customerNameInput = dialogView.findViewById<EditText>(R.id.etName)
        val customerPhoneInput = dialogView.findViewById<EditText>(R.id.etPhone)

        AlertDialog.Builder(this)
            .setTitle("Add Customer")
            .setView(dialogView)
            .setPositiveButton("Add") { dialog, _ ->
                val name = customerNameInput.text.toString()
                val phoneNumber = customerPhoneInput.text.toString()

                if (name.isNotEmpty() && phoneNumber.isNotEmpty()) {
                    val newCustomer = Customer(name = name, phoneNumber = phoneNumber)
                    appViewModel.insertCustomer(newCustomer) // Add the customer
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }
}

package com.barleyan.managementoko

import CustomerAdapter
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.AppViewModel
import com.example.project.Customer
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var appViewModel: AppViewModel
    private lateinit var customerAdapter: CustomerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // Set up RecyclerView with GridLayoutManager
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        // Initialize adapter with onEdit and onDelete callbacks
        customerAdapter = CustomerAdapter(
            onEdit = { customer ->
                showEditCustomerDialog(customer)
            },
            onDelete = { customer ->
                deleteCustomer(customer)
            }
        )
        recyclerView.adapter = customerAdapter

        // Set up ViewModel and observe changes
        appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)
        appViewModel.allCustomers.observe(this) { customers ->
            customers?.let { customerAdapter.submitList(it) }
        }

        // FloatingActionButton to add a new customer
        val fabAdd: FloatingActionButton = findViewById(R.id.fabAdd)
        fabAdd.setOnClickListener {
            showAddCustomerDialog()
        }
    }

    private fun showAddCustomerDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_customer, null)
        val customerNameInput = dialogView.findViewById<EditText>(R.id.etName)
        val customerPhoneInput = dialogView.findViewById<EditText>(R.id.etPhone)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Tambah Client")
            .setView(dialogView)
            .setPositiveButton("Tambah", null)
            .setNegativeButton("Batal", null)
            .create()

        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val name = customerNameInput.text.toString().trim()
            val phoneNumber = customerPhoneInput.text.toString().trim()

            if (name.isNotEmpty() && phoneNumber.isNotEmpty()) {
                val newCustomer = Customer(name = name, phoneNumber = phoneNumber)
                appViewModel.insertCustomer(newCustomer)
                dialog.dismiss()
            } else {
                if (name.isEmpty()) customerNameInput.error = "Nama tidak boleh kosong"
                if (phoneNumber.isEmpty()) customerPhoneInput.error = "Nomor telepon tidak boleh kosong"
            }
        }
    }

    private fun showEditCustomerDialog(customer: Customer) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_customer, null)
        val customerNameInput = dialogView.findViewById<EditText>(R.id.etName)
        val customerPhoneInput = dialogView.findViewById<EditText>(R.id.etPhone)

        // Populate fields with existing data
        customerNameInput.setText(customer.name)
        customerPhoneInput.setText(customer.phoneNumber)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Edit Client")
            .setView(dialogView)
            .setPositiveButton("Simpan", null)
            .setNegativeButton("Batal", null)
            .create()

        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val updatedName = customerNameInput.text.toString().trim()
            val updatedPhoneNumber = customerPhoneInput.text.toString().trim()

            if (updatedName.isNotEmpty() && updatedPhoneNumber.isNotEmpty()) {
                val updatedCustomer = customer.copy(name = updatedName, phoneNumber = updatedPhoneNumber)
                appViewModel.updateCustomer(updatedCustomer)
                dialog.dismiss()
            } else {
                if (updatedName.isEmpty()) customerNameInput.error = "Nama tidak boleh kosong"
                if (updatedPhoneNumber.isEmpty()) customerPhoneInput.error = "Nomor telepon tidak boleh kosong"
            }
        }
    }

    private fun deleteCustomer(customer: Customer) {
        appViewModel.deleteCustomer(customer)
        Toast.makeText(this, "${customer.name} telah dihapus", Toast.LENGTH_SHORT).show()
    }
}

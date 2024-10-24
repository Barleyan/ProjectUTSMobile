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

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        customerAdapter = CustomerAdapter(mutableListOf())
        recyclerView.adapter = customerAdapter

        appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)

        appViewModel.allCustomers.observe(this, Observer { customers ->
            customers?.let {
                customerAdapter.updateCustomers(it)
            }
        })

        val fabAdd: FloatingActionButton = findViewById(R.id.fabAdd)
        fabAdd.setOnClickListener {
            showAddCustomerDialog()
        }
    }

    private fun showAddCustomerDialog() {
        // Inflate the layout for the dialog
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_customer, null)
        val customerNameInput = dialogView.findViewById<EditText>(R.id.etName)
        val customerPhoneInput = dialogView.findViewById<EditText>(R.id.etPhone)


        val dialog = AlertDialog.Builder(this)
            .setTitle("Tambah Client")
            .setView(dialogView)
            .setPositiveButton("Tambah") { _, _ -> }
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

    private fun deleteCustomer(customer: Customer) {
        appViewModel.deleteCustomer(customer)
        Toast.makeText(this, "${customer.name} has been deleted", Toast.LENGTH_SHORT).show()
    }
}
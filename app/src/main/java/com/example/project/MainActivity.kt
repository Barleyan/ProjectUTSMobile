package com.barleyan.managementoko

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
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

        val nextButton = findViewById<Button>(R.id.nextID)
        nextButton.setOnClickListener {
            // Start the ProductCustomerActivity when Next is clicked
            val intent = Intent(this, MainActivityProduct::class.java)
            startActivity(intent)
        }


        // Setup RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize adapter with empty list
        customerAdapter = CustomerAdapter(mutableListOf())
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
            .setPositiveButton("Tambah") { dialog, which ->
                // Ketika tombol "Tambah" ditekan, ambil input dan simpan data baru
                val name = customerNameInput.text.toString()
                val phoneNumber = customerPhoneInput.text.toString()

                if (name.isNotEmpty() && phoneNumber.isNotEmpty()) {
                    val newCustomer = Customer(name = name, phoneNumber = phoneNumber)
                    appViewModel.insertCustomer(newCustomer) // Menyimpan data ke ViewModel
                }
            }
            .setNegativeButton("Batal", null)
            .create()
            .show()
    }


    // Fungsi untuk menghapus pelanggan dari ViewModel
    fun deleteCustomer(customer: Customer) {
        appViewModel.deleteCustomer(customer) // Memanggil fungsi delete di ViewModel
    }
}

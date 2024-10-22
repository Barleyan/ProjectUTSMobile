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
import com.example.project.Product
import com.example.project.ProductAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivityProduct : AppCompatActivity() {

    lateinit var appViewModel: AppViewModel
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        val nextButton = findViewById<Button>(R.id.nextID2)
        nextButton.setOnClickListener {
            // Start the ProductCustomerActivity when Next is clicked
            val intent = Intent(this, MainActivityTransaction::class.java)
            startActivity(intent)
        }

        val prevButton = findViewById<Button>(R.id.prevID2)
        prevButton.setOnClickListener {
            // Start the ProductCustomerActivity when Next is clicked
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


        // Setup RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize adapter with empty list
        productAdapter = ProductAdapter(mutableListOf())
        recyclerView.adapter = productAdapter

        appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)

        // Observing data and updating UI
        appViewModel.allProducts.observe(this, Observer { products ->
            products?.let {
                productAdapter.updateProducts(it) // Update RecyclerView with customer data
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
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_product, null)
        val productNameInput = dialogView.findViewById<EditText>(R.id.etName2)
        val productPriceInput = dialogView.findViewById<EditText>(R.id.etPrice)
        val productStockInput = dialogView.findViewById<EditText>(R.id.etStok)

        // Membuat dialog
        AlertDialog.Builder(this)
            .setTitle("Tambah Product")
            .setView(dialogView)
            .setPositiveButton("Tambah") { dialog, which ->
                // Ketika tombol "Tambah" ditekan, ambil input dan simpan data baru
                var name = productNameInput.text.toString()
                var price = productPriceInput.text.toString()
                var stock = productStockInput.text.toString()

                if (name.isNotEmpty() && price.isNotEmpty() && stock.isNotEmpty()) {
                    val newProduct = Product(name2 = name, price = price, stock = stock)
                    appViewModel.insertProduct(newProduct) // Menyimpan data ke ViewModel
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

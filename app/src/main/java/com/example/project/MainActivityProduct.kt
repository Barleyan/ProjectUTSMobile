package com.barleyan.managementoko

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.AppViewModel
import com.example.project.Product
import com.example.project.ProductAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivityProduct : AppCompatActivity() {

    lateinit var appViewModel: AppViewModel
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // Set up GridLayoutManager with 2 columns
        val gridLayoutManager = GridLayoutManager(this, 2)

        // Optionally, define how many spans an item should occupy
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                // Example: You can customize how many columns an item occupies
                return when (position % 3) {
                    0 -> 2 // Every third item takes up 2 columns
                    else -> 1 // Other items take up 1 column
                }
            }
        }

        recyclerView.layoutManager = gridLayoutManager

        // Initialize the adapter (no need for mutable list now)
        productAdapter = ProductAdapter()
        recyclerView.adapter = productAdapter

        // ViewModel initialization
        appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)

        // Observe live data for products and submit the list to the adapter
        appViewModel.allProducts.observe(this, Observer { products ->
            products?.let {
                productAdapter.submitList(it)  // Use submitList instead of updateProducts
            }
        })

        // Floating action button for adding new products
        val fabAdd: FloatingActionButton = findViewById(R.id.fabAdd)
        fabAdd.setOnClickListener {
            showAddProductDialog()
        }
    }

    // Function to show the dialog for adding a new product
    private fun showAddProductDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_product, null)
        val productNameInput = dialogView.findViewById<EditText>(R.id.etName2)
        val productPriceInput = dialogView.findViewById<EditText>(R.id.etPrice)
        val productStockInput = dialogView.findViewById<EditText>(R.id.etStok)

        AlertDialog.Builder(this)
            .setTitle("Tambah Product")
            .setView(dialogView)
            .setPositiveButton("Tambah") { dialog, which ->
                val name = productNameInput.text.toString()
                val price = productPriceInput.text.toString()
                val stock = productStockInput.text.toString()

                if (name.isNotEmpty() && price.isNotEmpty() && stock.isNotEmpty()) {
                    val newProduct = Product(name2 = name, price = price, stock = stock)
                    appViewModel.insertProduct(newProduct)  // Insert the new product via ViewModel
                }
            }
            .setNegativeButton("Batal", null)
            .create()
            .show()
    }
}

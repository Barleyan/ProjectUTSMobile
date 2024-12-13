package com.barleyan.managementoko.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.barleyan.managementoko.AppViewModel
import com.barleyan.managementoko.R
import com.example.project.Product
import com.example.project.ProductAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProductFragment : Fragment() {

    private lateinit var appViewModel: AppViewModel
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        // Initialize the adapter and set it to the RecyclerView
        appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)
        productAdapter = ProductAdapter(
            onEdit = { product ->
                // Handle the product edit action
                showEditProductDialog(product)
            },
            onDelete = { product ->
                appViewModel.deleteProduct(product)
                Toast.makeText(requireContext(), "Produk dihapus", Toast.LENGTH_SHORT).show()
            }
        )
        recyclerView.adapter = productAdapter

        // Observe LiveData for all products and update the adapter when data changes
        appViewModel.allProducts.observe(viewLifecycleOwner) { products ->
            products?.let {
                productAdapter.submitList(it)
            }
        }

        // Handle the FloatingActionButton click to add a new product
        val fabAdd: FloatingActionButton = view.findViewById(R.id.fabAdd)
        fabAdd.setOnClickListener {
            showAddProductDialog()
        }
    }

    private fun showAddProductDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_product, null)
        val productNameInput = dialogView.findViewById<EditText>(R.id.etName2)
        val productPriceInput = dialogView.findViewById<EditText>(R.id.etPrice)
        val productStockInput = dialogView.findViewById<EditText>(R.id.etStok)

        AlertDialog.Builder(requireContext())
            .setTitle("Tambah Produk")
            .setView(dialogView)
            .setPositiveButton("Tambah") { _, _ ->
                val name = productNameInput.text.toString().trim()
                val price = productPriceInput.text.toString().trim()
                val stock = productStockInput.text.toString().trim()

                if (name.isNotEmpty() && price.isNotEmpty() && stock.isNotEmpty()) {
                    try {
                        val newProduct = Product(name = name, price = price.toDouble().toString(), stock = stock.toInt().toString())
                        appViewModel.insertProduct(newProduct)
                        Toast.makeText(requireContext(), "Produk berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "Gagal menambahkan produk: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal", null)
            .create()
            .show()
    }

    private fun showEditProductDialog(product: Product) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_product, null)
        val productNameInput = dialogView.findViewById<EditText>(R.id.etName2)
        val productPriceInput = dialogView.findViewById<EditText>(R.id.etPrice)
        val productStockInput = dialogView.findViewById<EditText>(R.id.etStok)

        // Pre-fill the inputs with existing product details
        productNameInput.setText(product.name)
        productPriceInput.setText(product.price)
        productStockInput.setText(product.stock)

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Produk")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                val name = productNameInput.text.toString().trim()
                val price = productPriceInput.text.toString().trim()
                val stock = productStockInput.text.toString().trim()

                if (name.isNotEmpty() && price.isNotEmpty() && stock.isNotEmpty()) {
                    try {
                        val updatedProduct = product.copy(name = name, price = price, stock = stock)
                        appViewModel.updateProduct(updatedProduct)
                        Toast.makeText(requireContext(), "Produk berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "Gagal memperbarui produk: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal", null)
            .create()
            .show()
    }
}
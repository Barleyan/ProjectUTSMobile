package com.barleyan.managementoko

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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.project.AppViewModel
import com.example.project.Product
import com.example.project.ProductAdapter

class ProductFragment : Fragment() {

    private lateinit var appViewModel: AppViewModel
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        setupRecyclerView(recyclerView)

        // Initialize ViewModel
        appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)

        // Observe product data from ViewModel
        appViewModel.allProducts.observe(viewLifecycleOwner) { products ->
            products?.let {
                productAdapter.submitList(it) // Update the adapter's list
            }
        }

        // Setup FloatingActionButton for adding new products
        val fabAdd: FloatingActionButton = view.findViewById(R.id.fabAdd)
        fabAdd.setOnClickListener {
            showAddProductDialog()
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        // Use a GridLayoutManager with 2 columns
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.layoutManager = gridLayoutManager

        // Initialize the adapter and pass the context correctly
        productAdapter = ProductAdapter(
            requireContext(), // Pass the context here
            onDelete = { product ->
                appViewModel.deleteProduct(product)
                Toast.makeText(requireContext(), "${product.name2} telah dihapus", Toast.LENGTH_SHORT).show()
            },
            onEdit = { product ->
                showEditProductDialog(product)
            }
        )
        recyclerView.adapter = productAdapter
    }

    private fun showAddProductDialog() {
        // Inflate the custom dialog view
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_product, null)
        val productNameInput = dialogView.findViewById<EditText>(R.id.etName2)
        val productPriceInput = dialogView.findViewById<EditText>(R.id.etPrice)
        val productStockInput = dialogView.findViewById<EditText>(R.id.etStok)

        // Build the dialog
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Tambah Produk")
            .setView(dialogView)
            .setPositiveButton("Tambah") { _, _ -> }
            .setNegativeButton("Batal", null)
            .create()

        dialog.show()

        // Validate input before adding a product
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val name = productNameInput.text.toString().trim()
            val price = productPriceInput.text.toString().trim()
            val stock = productStockInput.text.toString().trim()

            if (name.isNotEmpty() && price.isNotEmpty() && stock.isNotEmpty()) {
                val newProduct = Product(name2 = name, price = price, stock = stock)
                appViewModel.insertProduct(newProduct)
                dialog.dismiss()
            } else {
                // Show error messages
                if (name.isEmpty()) productNameInput.error = "Nama tidak boleh kosong"
                if (price.isEmpty()) productPriceInput.error = "Harga tidak boleh kosong"
                if (stock.isEmpty()) productStockInput.error = "Stok tidak boleh kosong"
            }
        }
    }

    private fun showEditProductDialog(product: Product) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Edit Produk")

        val inflater = LayoutInflater.from(requireContext())
        val dialogView = inflater.inflate(R.layout.dialog_add_product, null)
        builder.setView(dialogView)

        val editName = dialogView.findViewById<EditText>(R.id.etName2)
        val editPrice = dialogView.findViewById<EditText>(R.id.etPrice)
        val editStock = dialogView.findViewById<EditText>(R.id.etStok)

        // Pre-fill the fields with current product data
        editName.setText(product.name2)
        editPrice.setText(product.price)
        editStock.setText(product.stock)

        builder.setPositiveButton("Simpan") { dialog, _ ->
            val newName = editName.text.toString().trim()
            val newPrice = editPrice.text.toString().trim()
            val newStock = editStock.text.toString().trim()

            if (newName.isNotEmpty() && newPrice.isNotEmpty() && newStock.isNotEmpty()) {
                product.name2 = newName
                product.price = newPrice
                product.stock = newStock
                appViewModel.updateProduct(product)
                dialog.dismiss()
            } else {
                if (newName.isEmpty()) editName.error = "Nama tidak boleh kosong"
                if (newPrice.isEmpty()) editPrice.error = "Harga tidak boleh kosong"
                if (newStock.isEmpty()) editStock.error = "Stok tidak boleh kosong"
            }
        }

        builder.setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }
}
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

    private lateinit var appViewModel: AppViewModel // ViewModel untuk mengelola data produk
    private lateinit var productAdapter: ProductAdapter // Adapter untuk RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Menginflate layout untuk fragment ini
        return inflater.inflate(R.layout.fragment_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        setupRecyclerView(recyclerView)

        // Inisialisasi ViewModel
        appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)

        // Mengamati data produk yang ada pada ViewModel
        appViewModel.allProducts.observe(viewLifecycleOwner) { products ->
            products?.let {
                productAdapter.submitList(it) // Memperbarui daftar produk pada adapter
            }
        }

        // Mengatur FloatingActionButton untuk menambah produk baru
        val fabAdd: FloatingActionButton = view.findViewById(R.id.fabAdd)
        fabAdd.setOnClickListener {
            showAddProductDialog() // Menampilkan dialog untuk menambah produk
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        // Menggunakan GridLayoutManager dengan 2 kolom untuk menampilkan produk
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.layoutManager = gridLayoutManager

        // Menginisialisasi adapter dan menetapkan aksi untuk menghapus dan mengedit produk
        productAdapter = ProductAdapter(
            requireContext(), // Pass context
            onDelete = { product -> // Aksi untuk menghapus produk
                appViewModel.deleteProduct(product) // Menghapus produk dari ViewModel
                Toast.makeText(requireContext(), "${product.name2} telah dihapus", Toast.LENGTH_SHORT).show()
            },
            onEdit = { product -> // Aksi untuk mengedit produk
                showEditProductDialog(product) // Menampilkan dialog edit produk
            }
        )
        recyclerView.adapter = productAdapter // Menetapkan adapter pada RecyclerView
    }

    private fun showAddProductDialog() {
        // Menginflate tampilan dialog untuk menambah produk
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_product, null)
        val productNameInput = dialogView.findViewById<EditText>(R.id.etName2)
        val productPriceInput = dialogView.findViewById<EditText>(R.id.etPrice)
        val productStockInput = dialogView.findViewById<EditText>(R.id.etStok)

        // Membangun dialog dengan tampilan dan tombol aksi
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Tambah Produk")
            .setView(dialogView)
            .setPositiveButton("Tambah") { _, _ -> }
            .setNegativeButton("Batal", null)
            .create()

        dialog.show()

        // Validasi input sebelum menambah produk
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val name = productNameInput.text.toString().trim()
            val price = productPriceInput.text.toString().trim()
            val stock = productStockInput.text.toString().trim()

            if (name.isNotEmpty() && price.isNotEmpty() && stock.isNotEmpty()) {
                val newProduct = Product(name2 = name, price = price, stock = stock)
                appViewModel.insertProduct(newProduct) // Menambahkan produk baru ke ViewModel
                dialog.dismiss() // Menutup dialog setelah produk ditambahkan
            } else {
                // Menampilkan error jika input kosong
                if (name.isEmpty()) productNameInput.error = "Nama tidak boleh kosong"
                if (price.isEmpty()) productPriceInput.error = "Harga tidak boleh kosong"
                if (stock.isEmpty()) productStockInput.error = "Stok tidak boleh kosong"
            }
        }
    }

    private fun showEditProductDialog(product: Product) {
        // Membuat dialog untuk mengedit produk yang sudah ada
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Edit Produk")

        val inflater = LayoutInflater.from(requireContext())
        val dialogView = inflater.inflate(R.layout.dialog_add_product, null)
        builder.setView(dialogView)

        val editName = dialogView.findViewById<EditText>(R.id.etName2)
        val editPrice = dialogView.findViewById<EditText>(R.id.etPrice)
        val editStock = dialogView.findViewById<EditText>(R.id.etStok)

        // Mengisi field dengan data produk yang ada
        editName.setText(product.name2)
        editPrice.setText(product.price)
        editStock.setText(product.stock)

        builder.setPositiveButton("Simpan") { dialog, _ ->
            val newName = editName.text.toString().trim()
            val newPrice = editPrice.text.toString().trim()
            val newStock = editStock.text.toString().trim()

            // Validasi input sebelum menyimpan perubahan
            if (newName.isNotEmpty() && newPrice.isNotEmpty() && newStock.isNotEmpty()) {
                product.name2 = newName
                product.price = newPrice
                product.stock = newStock
                appViewModel.updateProduct(product) // Memperbarui produk di ViewModel
                dialog.dismiss() // Menutup dialog setelah produk diperbarui
            } else {
                // Menampilkan error jika input kosong
                if (newName.isEmpty()) editName.error = "Nama tidak boleh kosong"
                if (newPrice.isEmpty()) editPrice.error = "Harga tidak boleh kosong"
                if (newStock.isEmpty()) editStock.error = "Stok tidak boleh kosong"
            }
        }

        builder.setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }
}
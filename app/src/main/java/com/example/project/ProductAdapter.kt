package com.example.project

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.barleyan.managementoko.R

// Adapter untuk menampilkan daftar produk di RecyclerView
class ProductAdapter(
    private val context: Context, // Untuk mengakses Context, misalnya untuk menampilkan AlertDialog
    private val onDelete: (Product) -> Unit, // Fungsi callback ketika tombol delete ditekan
    private val onEdit: (Product) -> Unit // Fungsi callback ketika tombol edit ditekan
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

    // ViewHolder untuk setiap item produk
    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productName: TextView = view.findViewById(R.id.tvProductName)
        val productPrice: TextView = view.findViewById(R.id.tvProductPrice)
        val productStock: TextView = view.findViewById(R.id.tvProductStock)
        val btnDeleteProduct: ImageButton = view.findViewById(R.id.btnDeleteProduct)
        val btnEditProduct: ImageButton = view.findViewById(R.id.btnEditProduct)
    }

    // Fungsi untuk membuat ViewHolder baru
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        // Menampilkan layout item produk
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(itemView)
    }

    // Fungsi untuk mengikat data produk ke setiap ViewHolder
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position) // Mengambil data produk di posisi tertentu
        holder.productName.text = product.name2 // Menampilkan nama produk
        holder.productPrice.text = product.price // Menampilkan harga produk
        holder.productStock.text = product.stock // Menampilkan stok produk

        // Mengatur aksi ketika tombol delete ditekan
        holder.btnDeleteProduct.setOnClickListener {
            showDeleteConfirmationDialog(product)
        }

        // Mengatur aksi ketika tombol edit ditekan
        holder.btnEditProduct.setOnClickListener {
            showEditProductDialog(product)
        }
    }

    // Menampilkan dialog konfirmasi untuk menghapus produk
    private fun showDeleteConfirmationDialog(product: Product) {
        AlertDialog.Builder(context)
            .setTitle("Hapus Produk") // Judul dialog
            .setMessage("Anda yakin ingin menghapus produk ${product.name2}?") // Pesan konfirmasi
            .setPositiveButton("Ya") { _, _ -> onDelete(product) } // Menangani aksi ketika "Ya" ditekan
            .setNegativeButton("Batal", null) // Menangani aksi ketika "Batal" ditekan
            .show()
    }

    // Menampilkan dialog untuk mengedit produk
    private fun showEditProductDialog(product: Product) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Edit Produk")

        // Menampilkan layout untuk mengedit produk
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.dialog_add_product, null)
        builder.setView(dialogView)

        val editName = dialogView.findViewById<TextView>(R.id.etName2)
        val editPrice = dialogView.findViewById<TextView>(R.id.etPrice)
        val editStock = dialogView.findViewById<TextView>(R.id.etStok)

        // Menampilkan data produk yang akan diedit
        editName.text = product.name2
        editPrice.text = product.price
        editStock.text = product.stock

        builder.setPositiveButton("Simpan") { dialog, _ ->
            // Membuat objek produk yang sudah diperbarui
            val updatedProduct = product.copy(
                name2 = editName.text.toString(),
                price = editPrice.text.toString(),
                stock = editStock.text.toString()
            )
            onEdit(updatedProduct) // Mengirimkan produk yang sudah diperbarui
            dialog.dismiss()
        }

        builder.setNegativeButton("Batal", null) // Menangani aksi ketika "Batal" ditekan
        builder.show()
    }

    // Custom DiffUtil callback untuk memeriksa perbedaan antara produk
    class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        // Membandingkan apakah item yang lama dan baru memiliki ID yang sama
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        // Membandingkan apakah konten item yang lama dan baru sama
        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
}
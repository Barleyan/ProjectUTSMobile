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

class ProductAdapter(
    private val context: Context,
    private val onDelete: (Product) -> Unit,
    private val onEdit: (Product) -> Unit
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productName: TextView = view.findViewById(R.id.tvProductName)
        val productPrice: TextView = view.findViewById(R.id.tvProductPrice)
        val productStock: TextView = view.findViewById(R.id.tvProductStock)
        val btnDeleteProduct: ImageButton = view.findViewById(R.id.btnDeleteProduct)
        val btnEditProduct: ImageButton = view.findViewById(R.id.btnEditProduct)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.productName.text = product.name2
        holder.productPrice.text = product.price
        holder.productStock.text = product.stock

        holder.btnDeleteProduct.setOnClickListener {
            showDeleteConfirmationDialog(product)
        }

        holder.btnEditProduct.setOnClickListener {
            showEditProductDialog(product)
        }
    }

    private fun showDeleteConfirmationDialog(product: Product) {
        AlertDialog.Builder(context)
            .setTitle("Hapus Produk")
            .setMessage("Anda yakin ingin menghapus produk ${product.name2}?")
            .setPositiveButton("Ya") { _, _ -> onDelete(product) }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showEditProductDialog(product: Product) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Edit Produk")

        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.dialog_add_product, null)
        builder.setView(dialogView)

        val editName = dialogView.findViewById<TextView>(R.id.etName2)
        val editPrice = dialogView.findViewById<TextView>(R.id.etPrice)
        val editStock = dialogView.findViewById<TextView>(R.id.etStok)

        editName.text = product.name2
        editPrice.text = product.price
        editStock.text = product.stock

        builder.setPositiveButton("Simpan") { dialog, _ ->
            val updatedProduct = product.copy(
                name2 = editName.text.toString(),
                price = editPrice.text.toString(),
                stock = editStock.text.toString()
            )
            onEdit(updatedProduct)
            dialog.dismiss()
        }

        builder.setNegativeButton("Batal", null)
        builder.show()
    }

    // Custom DiffUtil callback for product items
    class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
}
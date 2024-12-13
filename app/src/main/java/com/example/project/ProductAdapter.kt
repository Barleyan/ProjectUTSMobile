package com.barleyan.managementoko

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.barleyan.managementoko.fragments.ProductFragment

class ProductAdapter(private val viewModel: AppViewModel) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

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
        holder.productName.text = product.name
        holder.productPrice.text = product.price
        holder.productStock.text = product.stock

        holder.btnDeleteProduct.setOnClickListener {
            showDeleteConfirmationDialog(holder.itemView.context, product)
        }

        holder.btnEditProduct.setOnClickListener {
            showEditProductDialog(holder.itemView.context, product)
        }
    }

    private fun showDeleteConfirmationDialog(context: Context, product: Product) {
        AlertDialog.Builder(context)
            .setTitle("Hapus Produk")
            .setMessage("Anda yakin ingin menghapus produk ${product.name}?")
            .setPositiveButton("Ya") { _, _ ->
                viewModel.deleteProduct(product)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showEditProductDialog(context: Context, product: Product) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Edit Produk")

        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.dialog_add_product, null)
        builder.setView(dialogView)

        val editName = dialogView.findViewById<EditText>(R.id.etName2)
        val editPrice = dialogView.findViewById<EditText>(R.id.etPrice)
        val editStock = dialogView.findViewById<EditText>(R.id.etStok)

        editName.setText(product.name)
        editPrice.setText(product.price)
        editStock.setText(product.stock)

        builder.setPositiveButton("Simpan") { _, _ ->
            val newName = editName.text.toString()
            val newPrice = editPrice.text.toString()
            val newStock = editStock.text.toString()
            if (newName.isNotBlank() && newPrice.isNotBlank() && newStock.isNotBlank()) {
                product.name = newName
                product.price = newPrice
                product.stock = newStock
                viewModel.updateProduct(product)
            }
        }
        builder.setNegativeButton("Batal", null)

        builder.show()
    }

    class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
}
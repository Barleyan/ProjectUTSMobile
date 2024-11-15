package com.example.project

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
import com.barleyan.managementoko.MainActivityProduct
import com.barleyan.managementoko.R

class ProductAdapter : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

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
            showDeleteConfirmationDialog(holder.itemView.context, product, position)
        }

        holder.btnEditProduct.setOnClickListener {
            showEditProductDialog(holder.itemView.context, product, position)
        }
    }

    private fun showDeleteConfirmationDialog(context: Context, product: Product, position: Int) {
        AlertDialog.Builder(context)
            .setTitle("Hapus Produk")
            .setMessage("Anda yakin ingin menghapus ${product.name2}?")
            .setPositiveButton("Ya") { _, _ ->
                deleteProduct(context, product, position)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showEditProductDialog(context: Context, product: Product, position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Edit Produk")

        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.dialog_add_product, null)
        builder.setView(dialogView)

        val editName = dialogView.findViewById<EditText>(R.id.etName2)
        val editPrice = dialogView.findViewById<EditText>(R.id.etPrice)
        val editStock = dialogView.findViewById<EditText>(R.id.etStok)

        editName.setText(product.name2)
        editPrice.setText(product.price)
        editStock.setText(product.stock)

        builder.setPositiveButton("Simpan") { _, _ ->
            val newName = editName.text.toString()
            val newPrice = editPrice.text.toString()
            val newStock = editStock.text.toString()
            updateProduct(context, product, position, newName, newPrice, newStock)
        }
        builder.setNegativeButton("Batal", null)

        builder.show()
    }

    private fun updateProduct(context: Context, product: Product, position: Int, newName: String, newPrice: String, newStock: String) {
        product.name2 = newName
        product.price = newPrice
        product.stock = newStock
        (context as MainActivityProduct).appViewModel.updateProduct(product)

        notifyItemChanged(position)
    }

    private fun deleteProduct(context: Context, product: Product, position: Int) {
        (context as MainActivityProduct).appViewModel.deleteProduct(product)
        submitList(currentList.toMutableList().apply { removeAt(position) })
    }

    class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id // Ensure each Product has a unique identifier
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
}

package com.example.project

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.barleyan.managementoko.MainActivityProduct
import com.barleyan.managementoko.R

class ProductAdapter(private var productList: MutableList<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    // ViewHolder class for product items
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
        val product = productList[position]
        holder.productName.text = product.name2
        holder.productPrice.text = product.price
        holder.productStock.text = product.stock

        // Setup delete button functionality
        holder.btnDeleteProduct.setOnClickListener {
            showDeleteConfirmationDialog(holder.itemView.context, product, position)
        }

        // Setup edit button functionality
        holder.btnEditProduct.setOnClickListener {
            showEditProductDialog(holder.itemView.context, product, position)
        }
    }

    override fun getItemCount(): Int = productList.size

    fun updateProducts(newProducts: List<Product>) {
        productList.clear() // clear the existing list
        productList.addAll(newProducts) // add all new products
        notifyDataSetChanged()
    }

    // Show delete confirmation dialog
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

    // Show edit product dialog
    private fun showEditProductDialog(context: Context, product: Product, position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Edit Produk")

        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.dialog_add_product, null)
        builder.setView(dialogView)

        val editName = dialogView.findViewById<EditText>(R.id.etName2)
        val editPrice = dialogView.findViewById<EditText>(R.id.etPrice)
        val editStock = dialogView.findViewById<EditText>(R.id.etStok)

        // Set current product data in the EditText fields
        editName.setText(product.name2)
        editPrice.setText(product.price)
        editStock.setText(product.stock)

        // Set dialog actions
        builder.setPositiveButton("Simpan") { _, _ ->
            val newName = editName.text.toString()
            val newPrice = editPrice.text.toString()
            val newStock = editStock.text.toString()
            updateProduct(context, product, position, newName, newPrice, newStock)
        }
        builder.setNegativeButton("Batal", null)

        // Show the dialog
        builder.show()
    }

    // Update product details
    private fun updateProduct(context: Context, product: Product, position: Int, newName: String, newPrice: String, newStock: String) {
        // Update product data in ViewModel
        product.name2 = newName
        product.price = newPrice
        product.stock = newStock
        (context as MainActivityProduct).appViewModel.updateProduct(product)

        // Notify adapter of the change
        notifyItemChanged(position)
    }

    // Delete product and notify adapter
    private fun deleteProduct(context: Context, product: Product, position: Int) {
        // Call ViewModel function to delete product
        (context as MainActivityProduct).appViewModel.deleteProduct(product)

        // Remove item from list and notify adapter of changes
        productList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, productList.size)
    }
}

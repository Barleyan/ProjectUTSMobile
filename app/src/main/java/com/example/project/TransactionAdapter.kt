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
import com.barleyan.managementoko.fragments.TransactionFragment

class TransactionAdapter : ListAdapter<Transaction, TransactionAdapter.TransactionViewHolder>(TransactionDiffCallback()) {

    inner class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productID: TextView = view.findViewById(R.id.tvProductId)
        val customerID: TextView = view.findViewById(R.id.tvCustomerId)
        val quantity: TextView = view.findViewById(R.id.tvQuantity)
        val totalPrice: TextView = view.findViewById(R.id.tvTotalPrice)
        val btnDeleteTransaction: ImageButton = view.findViewById(R.id.btnDeleteTransaction)
        val btnEditTransaction: ImageButton = view.findViewById(R.id.btnEditTransaction)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = getItem(position)
        holder.productID.text = transaction.productId.toString()  // Convert to string for display
        holder.customerID.text = transaction.customerId.toString() // Convert to string for display
        holder.quantity.text = transaction.quantity.toString()  // Convert to string for display
        holder.totalPrice.text = transaction.totalPrice.toString()  // Convert to string for display

        holder.btnDeleteTransaction.setOnClickListener {
            showDeleteConfirmationDialog(holder.itemView.context, transaction, position)
        }

        holder.btnEditTransaction.setOnClickListener {
            showEditTransactionDialog(holder.itemView.context, transaction, position)
        }
    }

    private fun showDeleteConfirmationDialog(context: Context, transaction: Transaction, position: Int) {
        AlertDialog.Builder(context)
            .setTitle("Hapus Transaksi")
            .setMessage("Anda yakin ingin menghapus transaksi dengan ID pelanggan ${transaction.customerId}?")
            .setPositiveButton("Ya") { _, _ -> deleteTransaction(context, transaction, position) }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showEditTransactionDialog(context: Context, transaction: Transaction, position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Edit Transaksi")

        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.dialog_add_transaction, null)
        builder.setView(dialogView)

        val editProdukID = dialogView.findViewById<EditText>(R.id.etProdukTransaction)
        val editCustomerID = dialogView.findViewById<EditText>(R.id.etCustomerID)
        val editQuantity = dialogView.findViewById<EditText>(R.id.etQuantityID)
        val editTotalPrice = dialogView.findViewById<EditText>(R.id.etTotalPriceID)

        editProdukID.setText(transaction.productId.toString())  // Set the productId as string
        editCustomerID.setText(transaction.customerId.toString())  // Set the customerId as string
        editQuantity.setText(transaction.quantity.toString())  // Set quantity as string
        editTotalPrice.setText(transaction.totalPrice.toString())  // Set totalPrice as string

        builder.setPositiveButton("Simpan") { _, _ ->
            val newProduk = editProdukID.text.toString().toInt()
            val newCustomer = editCustomerID.text.toString().toInt()
            val newQuantity = editQuantity.text.toString().toInt()  // Convert to Int
            val newTotalPrice = editTotalPrice.text.toString().toInt()  // Convert to Int
            updateTransaction(context, transaction, position, newProduk, newCustomer, newQuantity, newTotalPrice)
        }
        builder.setNegativeButton("Batal", null)

        builder.show()
    }

    private fun updateTransaction(context: Context, transaction: Transaction, position: Int, newProduk: Int, newCustomer: Int, newQuantity: Int, newTotalPrice: Int) {
        transaction.productId = newProduk
        transaction.customerId = newCustomer
        transaction.quantity = newQuantity
        transaction.totalPrice = newTotalPrice
        (context as TransactionFragment).appViewModel.updateTransaction(transaction)

        notifyItemChanged(position)
    }

    private fun deleteTransaction(context: Context, transaction: Transaction, position: Int) {
        (context as TransactionFragment).appViewModel.deleteTransaction(transaction)
        val updatedList = currentList.toMutableList()
        updatedList.removeAt(position)
        submitList(updatedList) // Update list after deletion
    }

    class TransactionDiffCallback : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem == newItem
        }
    }
}
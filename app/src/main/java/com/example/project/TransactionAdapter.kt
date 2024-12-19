package com.barleyan.managementoko

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
import com.example.project.Transaction

class TransactionAdapter(
    private val context: Context,
    private val onDelete: (Transaction) -> Unit,
    private val onEdit: (Transaction) -> Unit
) : ListAdapter<Transaction, TransactionAdapter.TransactionViewHolder>(TransactionDiffCallback()) {

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
        holder.productID.text = transaction.productId
        holder.customerID.text = transaction.customerId
        holder.quantity.text = transaction.quantity
        holder.totalPrice.text = transaction.totalPrice

        holder.btnDeleteTransaction.setOnClickListener {
            showDeleteConfirmationDialog(transaction)
        }

        holder.btnEditTransaction.setOnClickListener {
            showEditTransactionDialog(transaction)
        }
    }

    private fun showDeleteConfirmationDialog(transaction: Transaction) {
        AlertDialog.Builder(context)
            .setTitle("Hapus Transaksi")
            .setMessage("Anda yakin ingin menghapus transaksi?")
            .setPositiveButton("Ya") { _, _ -> onDelete(transaction) }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showEditTransactionDialog(transaction: Transaction) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Edit Transaksi")

        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.dialog_add_transaction, null)
        builder.setView(dialogView)

        val editProductId = dialogView.findViewById<TextView>(R.id.etProdukTransaction)
        val editCustomerId = dialogView.findViewById<TextView>(R.id.etCustomerID)
        val editQuantity = dialogView.findViewById<TextView>(R.id.etQuantityID)
        val editTotalPrice = dialogView.findViewById<TextView>(R.id.etTotalPriceID)

        editProductId.text = transaction.productId
        editCustomerId.text = transaction.customerId
        editQuantity.text = transaction.quantity
        editTotalPrice.text = transaction.totalPrice

        builder.setPositiveButton("Simpan") { dialog, _ ->
            val updatedTransaction = transaction.copy(
                productId = editProductId.text.toString(),
                customerId = editCustomerId.text.toString(),
                quantity = editQuantity.text.toString(),
                totalPrice = editTotalPrice.text.toString()
            )
            onEdit(updatedTransaction)
            dialog.dismiss()
        }

        builder.setNegativeButton("Batal", null)
        builder.show()
    }

    // Custom DiffUtil callback for transaction items
    class TransactionDiffCallback : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem == newItem
        }
    }
}
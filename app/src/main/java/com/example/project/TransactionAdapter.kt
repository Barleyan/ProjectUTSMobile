package com.example.project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.barleyan.managementoko.R
import com.barleyan.managementoko.Transaction

class TransactionAdapter(
    private val onEdit: (Transaction) -> Unit,
    private val onDelete: (Transaction) -> Unit
) : ListAdapter<Transaction, TransactionAdapter.TransactionViewHolder>(TransactionDiffCallback()) {

    inner class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val transactionId: TextView = view.findViewById(R.id.tvTransactionId)
        val productId: TextView = view.findViewById(R.id.tvProductId)
        val customerId: TextView = view.findViewById(R.id.tvCustomerId)
        val quantity: TextView = view.findViewById(R.id.tvQuantity)
        val totalPrice: TextView = view.findViewById(R.id.tvTotalPrice)
        val amount: TextView = view.findViewById(R.id.tvTransactionAmount)
        val btnEdit: ImageButton = view.findViewById(R.id.btnEditTransaction)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDeleteTransaction)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = getItem(position)

        // Bind data to UI elements
        holder.transactionId.text = "Transaction ID: ${transaction.id}"
        holder.productId.text = "Product ID: ${transaction.productId}"
        holder.customerId.text = "Customer ID: ${transaction.customerId}"
        holder.quantity.text = "Quantity: ${transaction.quantity}"
        holder.totalPrice.text = "Total Price: ${transaction.totalPrice}"
        holder.amount.text = "Amount: ${transaction.amount}"

        // Set click listeners for buttons
        holder.btnEdit.setOnClickListener {
            onEdit(transaction)
        }

        holder.btnDelete.setOnClickListener {
            onDelete(transaction)
        }
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